package com.redspark.nycl.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Division {

  private static final Logger LOGGER = LoggerFactory.getLogger(Division.class);

  private DivisionConfiguration config;
  private Set<Fixture> divisionFixtureList = new HashSet<>();
  private List<Team> teams;

  public Division() {}

  public Division(DivisionConfiguration cfg, List<Team> teams) {
    this.teams = teams.stream().filter(t->t.getRank() ==  cfg.getDivisionNumber()).collect(Collectors.toList());
    this.config = cfg;
  }

  public boolean validateDivision() {
    return validateNumberOfGames() && validateHomeAndAway();
  }

  private boolean validateHomeAndAway() {
    for (Team clubTeam : teams) {
      if(config.isHomeAndAway()) {
        if(clubTeam.getHomeFixtureCount(this.divisionFixtureList) != clubTeam.getAwayFixtureCount(this.divisionFixtureList)) {
          return false;
        }
      } else {
        int games = teams.size() - 1;
        int minHomeAndAway = games / 2;
        if(clubTeam.getHomeFixtureCount(this.divisionFixtureList) >= minHomeAndAway && clubTeam.getAwayFixtureCount(this.divisionFixtureList) >= minHomeAndAway) {

        } else {
          LOGGER.info(clubTeam + " not equal home and away games");
          return false;
        }
      }
    }
    return true;
  }

  private boolean validateNumberOfGames() {
    if (config.isHomeAndAway()) {
      return divisionFixtureList.size() == (teams.size() - 1) * teams.size();
    } else {
      return divisionFixtureList.size() == (int)calcTriangularNumber(teams.size());
    }
  }

  private double calcTriangularNumber(double size) {
    return (size-1) * (size/2);
  }

  public void generateFixtures() {

    List<Team> randomOrderFirstTeams = new ArrayList(teams);
    List<Team> randomOrderSecondTeams = new ArrayList(teams);
    Collections.shuffle(randomOrderFirstTeams, new Random(System.nanoTime()));
    Collections.shuffle(randomOrderSecondTeams, new Random(System.nanoTime()));

    for (Team clubTeam : randomOrderFirstTeams) {

      if (!clubTeam.hasPreferredHomeDay()) {
        System.out.println(clubTeam + " has no preferred home day");
      }

      for (Team randomOrderSecondTeam : randomOrderSecondTeams) {
        if (!clubTeam.equals(randomOrderSecondTeam)) {
          if (config.isHomeAndAway()) {

            this.divisionFixtureList.add(new Fixture(clubTeam,
              randomOrderSecondTeam));

            //add reverse fixture
            this.divisionFixtureList.add(new Fixture(randomOrderSecondTeam,
              clubTeam));
          } else {
            if(!existsIgnoreHomeAndAway(clubTeam, randomOrderSecondTeam)) {
              createAndAddFixture(randomOrderSecondTeam, clubTeam);
            }
          }
        }
      }
    }
  }

  private boolean existsIgnoreHomeAndAway(Team team, Team team1) {
    Fixture f1 = new Fixture(team, team1);
    Fixture f2 = new Fixture(team1, team);
    return this.divisionFixtureList.contains(f1) || this.divisionFixtureList.contains(f2);
  }

  private void createAndAddFixture(Team team, Team team1) {
    int homeGames = team.getHomeFixtureCount(this.divisionFixtureList);
    int awayGames = team.getAwayFixtureCount(this.divisionFixtureList);
    int homeGames2 = team1.getHomeFixtureCount(this.divisionFixtureList);
    int awayGames2 = team1.getAwayFixtureCount(this.divisionFixtureList);

    if (homeGames == awayGames) {
      if (homeGames2 == awayGames2) {
        this.addUniqueFixture(new Fixture(team, team1));
      } else if (homeGames2 < awayGames2) {
        this.addUniqueFixture(new Fixture(team1, team));
      } else if (homeGames2 > awayGames2) {
        this.addUniqueFixture(new Fixture(team, team1));
      }
    } else if (homeGames < awayGames) {
      if (homeGames2 == awayGames2) {
        this.addUniqueFixture(new Fixture(team, team1));
      } else if (homeGames2 < awayGames2) {

        // team1 has less home games, but team 2 has more away games
        this.addUniqueFixture(new Fixture(team, team1));

      } else if (homeGames2 > awayGames2) {
        this.addUniqueFixture(new Fixture(team, team1));
      }
    } else {
      if (homeGames2 == awayGames2) {
        // add as away fixture
        this.addUniqueFixture(new Fixture(team1, team));
      } else if (homeGames2 < awayGames2) {
        // add as away fixture
        this.addUniqueFixture(new Fixture(team1, team));
      } else if (homeGames2 > awayGames2) {

        // team1 has more home games but team 2 has less away games
        this.addUniqueFixture(new Fixture(team1, team));
      }
    }
  }

  private void addUniqueFixture(Fixture f) {

    if(!this.divisionFixtureList.contains(f)) {
      this.divisionFixtureList.add(f);
    } else {
      LOGGER.info("Fixture already exists: " + f);
    }
  }


  public void setFixtureDates(List<Week> weeks, Set<Fixture> allFixtures) {
    for (Fixture fixture : divisionFixtureList) {
      LOGGER.info("Setting date for {}", fixture);
      setFixtureDate(weeks, fixture, fixture.getFixtureDate() == null, allFixtures);
    }
  }

  private void setFixtureDate(List<Week> weeks, Fixture fixture, boolean b, Set<Fixture> allFixtures) {
    if (b && fixture.isValid()) {
      for (Week week : weeks) {
        Date fixtureDate = getFixtureDateInWeek(fixture, week, allFixtures);
        if (null != fixtureDate) {
          fixture.setFixtureDate(fixtureDate);
          break;
        }
      }
    }
  }

  private Date getFixtureDateInWeek(Fixture fixture, Week week, Set<Fixture> allFixtures) {
    if (fixture.getHomeTeam().validate(week, this.divisionFixtureList) &&
      fixture.getAwayTeam().validate(week, this.divisionFixtureList)) {

      for (Date date : week.getDates()) {
        if (fixture.getHomeTeam().isValidDateForHomeTeam(allFixtures, date) &&
          fixture.getAwayTeam().isValidDateForAwayTeam(allFixtures, date)) {
          return date;
        }
      }
    } else {
      LOGGER.info("Fixture {} cannot be played in week {}", fixture, week);
    }
    return null;
  }


  public DivisionConfiguration getConfig() {
    return config;
  }

  public List<Team> getTeams() {
    return teams;
  }

  public Set<Fixture> getFixtures() {
    return this.divisionFixtureList;
  }

}
