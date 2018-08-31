package com.redspark.nycl;

import com.redspark.nycl.domain.*;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
  public ResponseEntity getClub(@RequestParam(name = "clubName") String clubName) {
    return new ResponseEntity(clubService.getClub(clubName), HttpStatus.OK);
  }

  @PostMapping(value = "/addteam", consumes = "application/json")
  @CrossOrigin(origins = "*")
  public ResponseEntity addTeam(@RequestBody Team team) {
    Club teamsClub = clubService.getClub(team.getClubName());
    teamsClub.getClubTeams().add(team);
    team.setClub(teamsClub);
    clubService.updateClub(team.getClubName(), teamsClub);
    return new ResponseEntity(HttpStatus.OK);
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
    Club stored = clubService.getClub(club.getClubName());
    stored.setEnterU11Cup(club.isEnterU11Cup());
    stored.setEnterU12Cup(club.isEnterU12Cup());
    stored.setEnterU14Cup(club.isEnterU14Cup());
    clubService.store(stored);
    return new ResponseEntity(HttpStatus.OK);
  }
}
