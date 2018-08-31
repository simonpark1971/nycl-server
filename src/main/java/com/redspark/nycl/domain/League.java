package com.redspark.nycl.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class League {

  private static final Logger LOGGER = LoggerFactory.getLogger(League.class);

  private LeagueConfiguration leagueConfig;
  private List<Division> divisions = new ArrayList<>();

  public League() {}

  public League(LeagueConfiguration leagueConfig, List<Team> teams) {
    this.leagueConfig = leagueConfig;
    for (DivisionConfiguration divisionConfiguration : leagueConfig.getDivisions()) {
      divisions.add(new Division(divisionConfiguration, teams));
    }
  }

  public LeagueConfiguration getLeagueConfig() {
    return leagueConfig;
  }

  public boolean validateLeague() {
    for (Division division : divisions) {
      boolean isValid = division.validateDivision();
      if(!isValid) {
        LOGGER.info(division.toString() + " is not valid.");
        return false;
      }
    }
    return true;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Division division : divisions) {
      sb.append(division);
    }

    return sb.toString();
  }

  public AgeGroup getAgeGroup() {
    return this.leagueConfig.getAgeGroup();
  }
  public List<Division> getDivisions() {
    return divisions;
  }

  public void generateFixtures() {
    for (Division division : divisions) {
      division.generateFixtures();
    }
  }

  public void setFixtureDates(List<Week> weeks, Set<Fixture> allFixtures) {
    for (Division division : divisions) {
      LOGGER.info("Setting fixtures for league {} division {}", this, division);
      division.setFixtureDates(weeks, allFixtures);
    }
  }

  public Set<Fixture> getClubFixtures(Club club) {
    return this.getDivisions().stream()
      .flatMap(division -> division.getFixtures().stream())
      .filter(f -> f.isForHomeOrAway(club))
      .collect(Collectors.toSet());
  }
}
