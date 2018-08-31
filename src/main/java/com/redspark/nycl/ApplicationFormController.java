package com.redspark.nycl;

import com.google.gson.Gson;
import com.opencsv.CSVWriter;
import com.redspark.nycl.domain.*;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import com.redspark.nycl.util.DomainUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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
  public String register(@RequestBody Club club) {
    club.getMainContact().setUsername("");
    clubService.store(club);
    return "registered";
  }

  @PostMapping(value = "/apply", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public String apply(@RequestBody Club club) {
    clubService.store(club);
    return "registered";
  }

  @PostMapping(value = "/complete", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public String completeApplication(@RequestBody Club club) {
    club.setApplicationStatus("complete");
    clubService.store(club);
    return "registered";
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Path("main")
  public void apply(@FormParam("clubName") String clubName,
                    @FormParam("applicantName") String applicantName,
                    @FormParam("applicantPosition") String applicantPosition,
                    @FormParam("clubContactName") String clubContactName,
                    @FormParam("clubContactAddress") String clubContactAddress,
                    @FormParam("clubContactPostcode") String clubContactPostcode,
                    @FormParam("clubContactHomePhone") String clubContactHomePhone,
                    @FormParam("clubContactMobilePhone") String clubContactMobilePhone,
                    @FormParam("clubContactEmail") String clubContactEmail,
                    @FormParam("fixtureContactName") String fixtureContactName,
                    @FormParam("fixtureContactAddress") String fixtureContactAddress,
                    @FormParam("fixtureContactPostcode") String fixtureContactPostcode,
                    @FormParam("fixtureContactHomePhone") String fixtureContactHomePhone,
                    @FormParam("fixtureContactMobilePhone") String fixtureContactMobilePhone,
                    @FormParam("fixtureContactEmail") String fixtureContactEmail,
                    @Context HttpServletResponse response) throws IOException{

    System.out.println("New Club:" + clubName);
    System.out.println("New applicantName:" + applicantName);
    System.out.println("New applicantPosition:" + applicantPosition);
    System.out.println("New clubContactName:" + clubContactName);
    System.out.println("New clubContactAddress:" + clubContactAddress);
    System.out.println("New Club:" + clubContactPostcode);
    System.out.println("New clubContactPostcode:" + clubContactHomePhone);
    System.out.println("New clubContactMobilePhone:" + clubContactMobilePhone);
    System.out.println("New clubContactEmail:" + clubContactEmail);
    System.out.println("New fixtureContactName:" + fixtureContactName);
    System.out.println("New fixtureContactAddress:" + fixtureContactAddress);
    System.out.println("New fixtureContactPostcode:" + fixtureContactPostcode);
    System.out.println("New fixtureContactHomePhone:" + fixtureContactHomePhone);
    System.out.println("New fixtureContactMobilePhone:" + fixtureContactMobilePhone);
    System.out.println("New fixtureContactEmail:" + fixtureContactEmail);


    Club club = new Club(clubName, 1);
    Contact mainContact = Contact.createContact(clubContactName,
      applicantPosition,
      clubContactAddress,
      clubContactPostcode,
      clubContactHomePhone,
      clubContactMobilePhone,
      clubContactEmail,
      Contact.ContactType.CLUB_CONTACT);
    club.setMainContact(mainContact);

    Contact fixturesContact = Contact.createContact(fixtureContactName,
      null,
      fixtureContactAddress,
      fixtureContactPostcode,
      fixtureContactHomePhone,
      fixtureContactMobilePhone,
      fixtureContactEmail,
      Contact.ContactType.FIXTURES_CONTACT);
    club.setFixturesContact(fixturesContact);

    clubService.store(club);

    response.sendRedirect("/nycl/addTeam.html");
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Path("team")
  public void addTeam(@QueryParam("another") Boolean another,
                      @QueryParam("clubName") String clubName,
                      @FormParam("ageGroupSelect") String ageGroup,
                      @FormParam("homeDaySelect") String homeDaySelect,
                      @FormParam("firstContactName") String firstContactName,
                      @FormParam("firstContactAddress") String firstContactAddress,
                      @FormParam("firstContactPostcode") String firstContactPostcode,
                      @FormParam("firstContactHomePhone") String firstContactHomePhone,
                      @FormParam("firstContactMobilePhone") String firstContactMobilePhone,
                      @FormParam("firstContactEmail") String firstContactEmail,
                      @FormParam("secondContactName") String secondContactName,
                      @FormParam("secondContactAddress") String  secondContactAddress,
                      @FormParam("secondContactPostcode") String  secondContactPostcode,
                      @FormParam("secondContactHomePhone") String secondContactHomePhone,
                      @FormParam("secondContactMobilePhone") String secondContactMobilePhone,
                      @FormParam("secondContactEmail") String secondContactEmail,
                      @Context HttpServletResponse response) throws IOException{

    System.out.println("Team age group:" + ageGroup);
    System.out.println("Preferred day:" + homeDaySelect);
    System.out.println("Primary contact name:" + firstContactName);
    System.out.println("Primary contact address:" + firstContactAddress);
    System.out.println("Primary contact postcode:" + firstContactPostcode);
    System.out.println("Primary contact home phone:" + firstContactHomePhone);
    System.out.println("Primary contact mobile phone:" + firstContactMobilePhone);
    System.out.println("Primary contact email address:" + firstContactEmail);
    System.out.println("Secondary contact name:" + secondContactName);
    System.out.println("Secondary contact address:" + secondContactAddress);
    System.out.println("Secondary contact postcode:" + secondContactPostcode);
    System.out.println("Secondary contact home phone:" + secondContactHomePhone);
    System.out.println("Secondary contact mobile phone:" + secondContactMobilePhone);
    System.out.println("Secondary contact email address:" + secondContactEmail);

    Club club = clubService.getClub(clubName);
    club.getClubTeams().add(Team.createTeam(
      club,
      DomainUtil.getAgeGroup(ageGroup),
      Contact.createContact(firstContactName,
        Contact.ContactType.FIRST_CONTACT.toString(),
        firstContactAddress,
        firstContactPostcode,
        firstContactHomePhone,
        firstContactMobilePhone,
        firstContactEmail,
        Contact.ContactType.FIRST_CONTACT),
      Contact.createContact(secondContactName,
        Contact.ContactType.SECOND_CONTACT.toString(),
        secondContactAddress,
        secondContactPostcode,
        secondContactHomePhone,
        secondContactMobilePhone,
        secondContactEmail,
        Contact.ContactType.SECOND_CONTACT), "",
      false, false, false,
      false, false, false));

    clubService.updateClub(club.getClubName(), club);

    if(another) {
      response.sendRedirect("/nycl/addTeam.html");
    } else {
      response.sendRedirect("/nycl/clubTeams.html");
    }
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
  @Path("team/delete")
  public void deleteTeam(@QueryParam("clubName")String clubName, @QueryParam("ageGroup") AgeGroup ageGroup) {
    Club club = clubService.getClub(clubName);
    if (club != null) {
      for (Team team : club.getClubTeams()) {
        if (team.getAgeGroup() == ageGroup && !team.isDeleted()) {
          team.delete();
          clubService.markTeamAsDeleted(club);
          return;
        }
      }

    }
  }

  private void filterDeletedTeams(Club club) {
    club.removeDeletedTeams();
  }

  @POST
  @Path("team/updaterank")
  public void updateTeamRank(@QueryParam("clubName") String clubName,
                             @QueryParam("ageGroup") AgeGroup ageGroup,
                             @QueryParam("rank") int rank) {

    Club club = clubService.getClub(clubName);
    if (club != null) {
      for (Team team : club.getClubTeams()) {
        if (team.getAgeGroup() == ageGroup && !team.isDeleted()) {
          team.setRank(rank);
          clubService.store(club);
          return;
        }
      }

    }

  }
}
