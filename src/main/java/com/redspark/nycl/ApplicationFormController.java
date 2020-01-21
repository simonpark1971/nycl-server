package com.redspark.nycl;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.redspark.nycl.domain.*;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

/**
 * Created by simonpark on 26/09/2016.
 */
@RestController
public class ApplicationFormController {

  @Autowired
  private SeasonConfigurationService seasonConfigurationService;

  @Autowired
  private ClubService clubService;

  @PostMapping(value = "/register", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity register(@RequestBody Club club) {
    clubService.store(club);
    return new ResponseEntity(clubService.getClub(club.getClubName()), HttpStatus.OK);
  }

  @PostMapping(value = "/apply", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity apply(@RequestBody Club club) {
    clubService.store(club);
    return new ResponseEntity(clubService.getClub(club.getClubName()), HttpStatus.OK);
  }

  @PostMapping(value = "/complete", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity completeApplication(@RequestBody Club club) {
    club.setApplicationStatus("complete");
    clubService.store(club);
    return new ResponseEntity(HttpStatus.OK);
  }

  @GET
  @Path("myteams")
  @Produces("application/json")
  public String getTeams(@QueryParam("clubName") String clubName) throws IOException{

    Gson gson = new Gson();
    return gson.toJson(clubService.getClub(clubName));
  }

  @GET
  @Path("allclubs")
  @Produces("application/json")
  public String getClubs() throws IOException{

    Gson gson = new Gson();
    return gson.toJson(clubService.getClubList());
  }

  @GET
  @Path("leagues")
  @Produces("application/json")
  public String getLeagues(@QueryParam("seasonTag") String season) {

    Gson gson = new Gson();
    return gson.toJson(seasonConfigurationService.getLeagues(season));
  }

  @GET
  @Path("club/downloads")
  @Produces("text/plain")
  public void downloadClubs(@Context HttpServletResponse response) throws Exception {
    response.setHeader("Content-Disposition", "attachment; filename=" + "nycl-clubs-2017" + ".csv");
    response.setContentType("text/csv");
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
    CSVWriter csvWriter = new CSVWriter(osw, ',');
    csvWriter.writeNext(new String [] {"Club Name", "Main Contact - Name",
      "Main Contact - Position", "Main Contact - Address", "Main Contact - Postcode", "Main Contact - Email",
      "Main Contact - Home Phone", "Main Contact - Mobile Phone",
      "Fixtures Contact - Name",
      "Fixtures Contact - Position", "Fixtures Contact - Address", "Fixtures Contact - Postcode", "Fixtures Contact - Email",
      "Fixtures Contact - Home Phone", "Fixtures Contact - Mobile Phone"});
    List<Club> clubs = clubService.getClubList();
    for (Club club : clubs) {
      club.removeDeletedTeams();
      if (club.getMainContact() != null && club.getFixturesContact() != null) {
        String[] csvLineArray = new String[]{club.getClubName(),
          club.getMainContact().getContactName(),
          club.getMainContact().getContactPosition(),
          club.getMainContact().getContactAddress(),
          club.getMainContact().getContactPostcode(),
          club.getMainContact().getContactEmail(),
          club.getMainContact().getContactHomePhone(),
          club.getMainContact().getContactMobilePhone(),
          club.getFixturesContact().getContactName(),
          club.getFixturesContact().getContactPosition(),
          club.getFixturesContact().getContactAddress(),
          club.getFixturesContact().getContactPostcode(),
          club.getFixturesContact().getContactEmail(),
          club.getFixturesContact().getContactHomePhone(),
          club.getFixturesContact().getContactMobilePhone()
        };
        csvWriter.writeNext(csvLineArray);
      }
    }
    csvWriter.flush();
    csvWriter.close();
  }

  @GET
  @Path("deleteclub")
  public void deleteClub(@QueryParam("clubName")String clubName) {
    clubService.markClubDeleted(clubName);
  }

  @GET
  @Path("contacts")
  @Produces("text/plain")
  public void downloadContacts(@Context HttpServletResponse response) throws Exception {
    response.setHeader("Content-Disposition", "attachment; filename=" + "nycl-contacts-2017" + ".csv");
    response.setContentType("text/csv");
    OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
    CSVWriter csvWriter = new CSVWriter(osw, ',');
    csvWriter.writeNext(new String [] {"Club Name", "Name",
      "Position",
      "Address",
      "Postcode",
      "Email",
      "Home Phone",
      "Mobile Phone"});
    List<Club> clubs = clubService.getClubList();
    for (Club club : clubs) {
      writeCsvLine(club.getMainContact(), "Main Contact", club.getClubName(), csvWriter);
      writeCsvLine(club.getFixturesContact(), "Fixtures Contact", club.getClubName(), csvWriter);
      for (Team team : club.getClubTeams()) {
        writeCsvLine(team.getPrimaryContact(), team.getAgeGroup().toString() + " Primary Contact", club.getClubName(), csvWriter);
        writeCsvLine(team.getSecondaryContact(), team.getAgeGroup().toString() + " Secondary Contact", club.getClubName(), csvWriter);
      }
    }
    csvWriter.flush();
    csvWriter.close();
  }

  private void writeCsvLine(Contact contact, String position, String clubName, CSVWriter writer) {
    if(contact != null) {
      String[] csvLineArray = new String[]{
        clubName,
        contact.getContactName(),
        position,
        contact.getContactAddress(),
        contact.getContactPostcode(),
        contact.getContactEmail(),
        contact.getContactHomePhone(),
        contact.getContactMobilePhone()
      };
      writer.writeNext(csvLineArray);
    }
  }

  @POST
  @Path("cup/add")
  public void addClubToCup(@QueryParam("clubName")String clubName, @QueryParam("competition")CupCompetition comp) {
    Club club = clubService.getClub(clubName);
    if (club != null) {
      club.addToCup(comp);
      clubService.store(club);
    }
  }

  @GET
  @Path("cups")
  @Produces("application/json")
  public String getCupCompetitions() {
//        List<Club> clubs = clubService.getClubList();
//        Map<CupCompetition, List<String>> cups = new HashMap<>();
//
//        for (Club club : clubs) {
//            if(club.getCupCompetitions() != null) {
//                for (CupCompetition cupCompetition : club.getCupCompetitions()) {
//                    if(!cups.containsKey(cupCompetition)) {
//                        cups.put(cupCompetition, new ArrayList<>());
//                    }
//                    cups.get(cupCompetition).add(club.getClubName());
//                }
//            }
//
//        }
//
//        Gson gson = new Gson();
//        return gson.toJson(cups);
    return null;
  }

  @POST
  @Path("league/updateClub")
  public void updateLeague(@QueryParam("ageGroup") AgeGroup ageGroup, @QueryParam("divisions") int divisions) {
//        League league = seasonConfigurationService.getLeague(ageGroup);
//        if(league == null) {
//            league = new League(new SeasonConfiguration.LeagueConfiguration(ageGroup));
//        }
//        //seasonConfigurationService.saveLeague(league);
  }

  @POST
  @Path("league/homeAndAway")
  public void updateLeague(@QueryParam("ageGroup") AgeGroup ageGroup, @QueryParam("homeAndAway") boolean homeAndAway) {
//      League league = seasonConfigurationService.getLeague(ageGroup);
//        if(league == null) {
//            league = new League(new SeasonConfiguration.LeagueConfiguration(ageGroup));
//        }
//      //seasonConfigurationService.saveLeague(league);
  }

  @POST
  @Path("team/updaterank")
  public void updateTeamRank(@QueryParam("clubName") String clubName,
                             @QueryParam("ageGroup") AgeGroup ageGroup,
                             @QueryParam("rank") int rank) {
    clubService.updateTeamRanking(clubName, ageGroup, rank);
  }

  private void filterDeletedTeams(Club club) {
    club.removeDeletedTeams();
  }
}
