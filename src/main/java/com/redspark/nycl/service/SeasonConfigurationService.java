package com.redspark.nycl.service;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Fixture;
import com.redspark.nycl.domain.League;
import com.redspark.nycl.domain.Season;

import java.util.List;
import java.util.Map;

public interface SeasonConfigurationService {

  League getLeague(String season, AgeGroup ageGroup);

  List<League> getLeagues(String season);

  Season createSeason(String seasonTag);

  Map getCupEntries(String year);

  Season get(String s);

  void saveSeason(String seasonYear, Season season);

  List<Fixture> generateFixtures(String seasonTag);

  void saveLeague(League league);
}
