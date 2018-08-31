package com.redspark.nycl;

import com.redspark.nycl.domain.Season;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.GregorianCalendar;

@RestController
public class FixtureController {

  public static final Calendar CURRENT_CALENDAR = new GregorianCalendar();
  private static final Logger LOGGER = LoggerFactory.getLogger(FixtureController.class);

  @Autowired
  private ClubService clubService;

  @Autowired
  private SeasonConfigurationService seasonConfigurationService;

  @GetMapping(value = "/getfixtures")
  @CrossOrigin(origins = "*")
  public ResponseEntity getFixtures(@RequestParam(name = "season") String seasonTag) {
    Season season = seasonConfigurationService.createSeason(seasonTag);
    seasonConfigurationService.generateFixtures(seasonTag);
    return new ResponseEntity(season, HttpStatus.OK);
  }
}
