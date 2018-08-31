package com.redspark.nycl.service.impl.postgresql;

import com.redspark.nycl.domain.*;
import com.redspark.nycl.persistence.*;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PostgresqlSeasonConfigurationService implements SeasonConfigurationService {

  private static final Calendar CALENDAR = new GregorianCalendar();
  public static final String CURRENT_YEAR = Integer.toString(CALENDAR.get(Calendar.YEAR));
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MM yyyy");

  @Autowired
  private PostgresqlSeasonConfigurationRepository postgresqlSeasonConfigurationRepository;

  @Autowired
  private PostgresqlClubRepository clubRepository;

  @Autowired
  private PostgresqlSeasonRepository seasonRepository;

  @Autowired
  private PostgresqlTeamRepository teamRepository;

  @Autowired
  private PostgresqlLeagueConfigurationRepository leagueConfigurationRepository;

  @Autowired
  private PostgresqlDivisionConfigurationRepository divisionConfigurationRepository;

  @Override
  public League getLeague(String season, AgeGroup ageGroup) {

    LeagueConfiguration leagueConfig = leagueConfigurationRepository.findLeagueConfigurationByAgeGroup(ageGroup);

    League league = new League(leagueConfig, this.postgresqlSeasonConfigurationRepository.findTeamsByAgeGroupEquals(
      ageGroup));
    return league;
  }

  @Override
  public List<League> getLeagues(String season) {
    return null;
  }

  @Override
  public Season createSeason(String seasonTag) {
    Season season = null;
    try {
      season = seasonRepository.findByName(seasonTag);
      if (null == season) {
        Date startDate = DATE_FORMAT.parse("01 05 " + CURRENT_YEAR);
        Date endDate = DATE_FORMAT.parse("31 07 " + CURRENT_YEAR);
        season = new Season(startDate, endDate, new SeasonConfiguration(seasonTag));
      }
    } catch( ParseException parseEx) {
      throw new RuntimeException(parseEx);
    }
    return season;
  }

  @Override
  public Map getCupEntries(String year) {
    Map cupMap = new HashMap();

    cupMap.put(AgeGroup.Under11, this.clubRepository.findClubsByEnterU11Cup(true));
    cupMap.put(AgeGroup.Under12, this.clubRepository.findClubsByEnterU12Cup(true));
    cupMap.put(AgeGroup.Under14, this.clubRepository.findClubsByEnterU14Cup(true));

    return cupMap;
  }

  @Override
  public Season get(String s) {
    Season season = seasonRepository.findByName(s);
    if (season == null) {
      season = createSeason(s);
    }
    return season;
  }

  @Override
  public void saveSeason(String seasonYear, Season season) {
    season.setName(seasonYear);
    for (LeagueConfiguration leagueConfiguration : season.getConfig().getAgeGroupConfigurations().values()) {
      divisionConfigurationRepository.save(leagueConfiguration.getDivisions());
    }
    leagueConfigurationRepository.save(season.getConfig().getAgeGroupConfigurations().values());
    seasonRepository.save(season);
  }

  @Override
  public List<Fixture> generateFixtures(String seasonTag) {
    Season currentSeason = createSeason(seasonTag);

    currentSeason.createFixtures(toHashMap());
    currentSeason.setFixtureDates();

    return currentSeason.getFixtures();
  }

  @Override
  public void saveLeague(League league) {
    for (Division division : league.getDivisions()) {
      for (Team team : division.getTeams()) {
        team.setClub(teamRepository.findOne(team.getId()).getClub());
      }
      teamRepository.save(division.getTeams());
    }
  }

  private Map<AgeGroup, List<Team>> toHashMap() {
    Map<AgeGroup, List<Team>> ageGroupTeams = new HashMap<>();
    for (AgeGroup ageGroup : AgeGroup.values()) {
      ageGroupTeams.put(ageGroup, teamRepository.findTeamsByAgeGroup(ageGroup));
    }
    return ageGroupTeams;
  }
}
