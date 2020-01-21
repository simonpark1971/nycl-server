package com.redspark.nycl;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Team;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;

/**
 * Created by simonpark on 26/09/2016.
 */
@RestController
public class ClubController {

  @Autowired
  private ClubService clubService;

  @Autowired
  private SeasonConfigurationService seasonConfigurationService;

  @GetMapping(value = "/getclubs")
  @CrossOrigin(origins = "*")
  public ResponseEntity getClubs() {
    return new ResponseEntity(clubService.getClubList(), HttpStatus.OK);
  }


  @GetMapping(value = "/status")
  @CrossOrigin(origins = "*")
  public ResponseEntity getStatus(@RequestParam(name = "clubName") String clubName) {
    return new ResponseEntity(clubService.getClub(clubName).getApplicationStatus(), HttpStatus.OK);
  }

  @GetMapping(value = "/getclub")
  @CrossOrigin(origins = "*")
  @Produces("application/json")
  public ResponseEntity getClub(@RequestParam(name = "id") String id) {
    return new ResponseEntity(clubService.getClubById(id), HttpStatus.OK);
  }

  @GetMapping(value = "/getclubbyusername")
  @CrossOrigin(origins = "*")
  @Produces("application/json")
  public ResponseEntity getClubByUser(@RequestParam(name = "username") String username) {
    return new ResponseEntity(clubService.getClubByUserName(username), HttpStatus.OK);
  }

  @PostMapping(value = "/addteam", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity addTeam(@RequestBody Team team) {
    clubService.addTeam(team);
    Club teamsClub = clubService.getClub(team.getClubName());
    return new ResponseEntity(teamsClub.getClubTeams(), HttpStatus.OK);
  }

  @GetMapping(value = "/getteams")
  @CrossOrigin(origins = "*")
  public ResponseEntity getTeams(@RequestParam(name = "clubName") String clubName) {
    Club teamsClub = clubService.getClub(clubName);
    return new ResponseEntity(teamsClub.getClubTeams(), HttpStatus.OK);
  }

  @PostMapping(value = "/updatecupentries", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity updateCupEntries(@RequestBody Club club) {
    clubService.updateCupEntries(club);
    return new ResponseEntity(HttpStatus.OK);
  }

  @DeleteMapping(value = "/team/delete/{teamId}")
  @CrossOrigin(origins = "*")
  public void deleteTeam(@PathVariable String teamId) {
    clubService.deleteTeam(teamId);
  }
}
