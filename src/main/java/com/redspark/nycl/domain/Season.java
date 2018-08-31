package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.redspark.nycl.service.impl.postgresql.PostgresqlSeasonConfigurationService;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.Collections.*;

@Entity
@Table(name = "season")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Season {

  private static final Logger LOGGER = LoggerFactory.getLogger(Season.class);

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;
  private Date startDate;
  private Date endDate;

  @Transient
  private List<Week> weeks;

  public List<League> getLeagues() {
    return leagues;
  }

  @Transient
  private List<League> leagues = new ArrayList<>();

  @OneToOne
  @MapsId
  private SeasonConfiguration config;
  private String name;

  public Season() {}

  public Season(Date start, Date end, SeasonConfiguration config) {

    this.startDate = start;
    this.endDate = end;
    this.setConfig(config);

    try {
      this.weeks = WeekBuilder.buildWeeks(startDate, endDate);
    } catch(Exception e) {
      e.printStackTrace();
    }

    LOGGER.info("Created a season that starts " + startDate.toString() + " and ends " + endDate.toString());
  }

  public List<Fixture> getFixtures() {
    List<Fixture> allFixtures = new ArrayList<>();
    for (League league : leagues) {
      for (Division division : league.getDivisions()) {
        allFixtures.addAll(division.getFixtures());
      }
    }
    allFixtures.sort(new Comparator<Fixture>() {
      @Override
      public int compare(Fixture o1, Fixture o2) {
        if(o1.getFixtureDate() == null && o2.getFixtureDate() == null) return 0;
        if(o1.getFixtureDate() == null) return 1;
        if(o2.getFixtureDate() == null) return -1;

        return o1.getFixtureDate().compareTo(o2.getFixtureDate());
      }
    });
    return allFixtures;
  }

  public void setFixtureDates() {
    try {
      if (weeks == null || weeks.isEmpty()) {
        this.startDate = PostgresqlSeasonConfigurationService.DATE_FORMAT.parse("30 04 " + PostgresqlSeasonConfigurationService.CURRENT_YEAR);
        this.endDate = PostgresqlSeasonConfigurationService.DATE_FORMAT.parse("31 07 " + PostgresqlSeasonConfigurationService.CURRENT_YEAR);
        this.weeks = WeekBuilder.buildWeeks(startDate, endDate);
      }
    } catch (ParseException pe) {
      LOGGER.error("Invalid start / end dates");
    }
    for (League league : leagues) {
      league.generateFixtures();
      league.setFixtureDates(weeks, allFixtures());
      league.validateLeague();
    }
  }

  private Set<Fixture> allFixtures() {
    Set<Division> divisions = leagues.stream().flatMap(league -> league.getDivisions().stream()).collect(Collectors.toSet());
    return divisions.stream().flatMap(div -> div.getFixtures().stream()).collect(Collectors.toSet());
  }

  public void createFixtures(Map<AgeGroup, List<Team>> map) {
    for (AgeGroup ageGroup : config.getAgeGroupConfigurations().keySet()) {
      leagues.add(new League(config.getAgeGroupConfigurations().get(ageGroup), map.get(ageGroup)));
    }
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public SeasonConfiguration getConfig() {
    return config;
  }
  public void setConfig(SeasonConfiguration config) {
    this.config = config;
  }
}
