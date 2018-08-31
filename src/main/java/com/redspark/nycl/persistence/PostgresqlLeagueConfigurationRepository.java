package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.LeagueConfiguration;
import org.springframework.data.repository.CrudRepository;

public interface PostgresqlLeagueConfigurationRepository extends CrudRepository<LeagueConfiguration, String> {

  LeagueConfiguration findLeagueConfigurationByAgeGroup(AgeGroup ageGroup);
}
