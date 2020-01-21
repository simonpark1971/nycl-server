package com.redspark.nycl;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.League;
import com.redspark.nycl.domain.Season;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    return new ResponseEntity(seasonService.getLeague("2019", AgeGroup.valueOf(league)), HttpStatus.OK);
  }

  @GetMapping(value = "/getcupentries")
  @CrossOrigin(origins = "*")
  public ResponseEntity getCupEntries() {
    return new ResponseEntity(seasonService.getCupEntries("2019"), HttpStatus.OK);
  }

  @GetMapping(value = "/getseason")
  @CrossOrigin(origins = "*")
  public ResponseEntity getSeason() {
    return new ResponseEntity(seasonService.get("2020"), HttpStatus.OK);
  }

  @PutMapping(value = "/saveseason")
  @CrossOrigin(origins = "*")
  public void saveSeason(@RequestBody Season season) {
    seasonService.saveSeason("2019", season);
  }

  @GetMapping(value = "/generatefixtures")
  @CrossOrigin(origins = "*")
  public void generateFixtures() {
    seasonService.generateFixtures("2019");
  }

  @PostMapping(value = "/updateleagues")
  @CrossOrigin(origins = "*")
  public void updateLeagues(@RequestBody League league) {
    seasonService.saveLeague(league);
  }
}
