package com.redspark.nycl;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.League;
import com.redspark.nycl.domain.Season;
import com.redspark.nycl.domain.Team;
import com.redspark.nycl.persistence.PostgresqlTeamRepository;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by simonpark on 26/09/2016.
 */
@RestController
public class AdminController {

  @Autowired
  private SeasonConfigurationService seasonService;

  @GetMapping(value = "/getleague")
  @CrossOrigin(origins = "*")
  public ResponseEntity getLeagueTeams(@RequestParam(name = "league") String league) {
    return new ResponseEntity(seasonService.getLeague("2018", AgeGroup.valueOf(league)), HttpStatus.OK);
  }

  @GetMapping(value = "/getcupentries")
  @CrossOrigin(origins = "*")
  public ResponseEntity getCupEntries() {
    return new ResponseEntity(seasonService.getCupEntries("2018"), HttpStatus.OK);
  }

  @GetMapping(value = "/getseason")
  @CrossOrigin(origins = "*")
  public ResponseEntity getSeason() {
    return new ResponseEntity(seasonService.get("2018"), HttpStatus.OK);
  }

  @PutMapping(value = "/saveseason")
  @CrossOrigin(origins = "*")
  public void saveSeason(@RequestBody Season season) {
    seasonService.saveSeason("2018", season);
  }

  @GetMapping(value = "/generatefixtures")
  @CrossOrigin(origins = "*")
  public void generateFixtures() {
    seasonService.generateFixtures("2018");
  }

  @PostMapping(value = "/updateleagues")
  @CrossOrigin(origins = "*")
  public void updateLeagues(@RequestBody League league) {
    seasonService.saveLeague(league);
  }
}
