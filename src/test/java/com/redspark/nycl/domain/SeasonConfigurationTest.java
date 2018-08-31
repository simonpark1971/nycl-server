package com.redspark.nycl.domain;

import org.junit.Test;

import static com.redspark.nycl.domain.AgeGroup.*;

public class SeasonConfigurationTest {

  @Test
  public void addsConfigToSeasonConfig() {

    SeasonConfiguration seasonConfig = new SeasonConfiguration("TEST");
  }

  private LeagueConfiguration getU10LeagueConfig() {
    LeagueConfiguration u10Config = new LeagueConfiguration(Under10,
      new DivisionConfiguration(1, true, Under10),
      new DivisionConfiguration(2, true, Under10));
    return u10Config;
  }

  private LeagueConfiguration getU11LeagueConfig() {
    LeagueConfiguration cfg = new LeagueConfiguration(Under11,
      new DivisionConfiguration(1, true, Under11),
      new DivisionConfiguration(2, false, Under11));
    return cfg;
  }

  private LeagueConfiguration getU12LeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(Under12,
      new DivisionConfiguration(1,true, Under12),
      new DivisionConfiguration(2, true, Under12));
    return config;
  }

  private LeagueConfiguration getU13LeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(Under13,
      new DivisionConfiguration(1, true, Under13),
      new DivisionConfiguration(2, false, Under13));
    return config;
  }

  private LeagueConfiguration getU14LeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(AgeGroup.Under14,
      new DivisionConfiguration(1, false, Under14));
    return config;
  }

  private LeagueConfiguration getU15LeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(AgeGroup.Under15,
      new DivisionConfiguration(1, false, Under15));
    return config;
  }

  private LeagueConfiguration getU138sLeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(AgeGroup.Under13_8S,
      new DivisionConfiguration(1, true, Under13_8S));
    return config;
  }

  private LeagueConfiguration getU158sLeagueConfig() {
    LeagueConfiguration config = new LeagueConfiguration(AgeGroup.Under15_8S,
      new DivisionConfiguration(1, true, Under15_8S));
    return config;
  }

  private LeagueConfiguration getGirlsLeagueConfig() {
    return new LeagueConfiguration(AgeGroup.GIRLS,
      new DivisionConfiguration(1, true, GIRLS));
  }

}
