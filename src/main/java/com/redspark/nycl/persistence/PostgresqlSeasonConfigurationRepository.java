package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgresqlSeasonConfigurationRepository extends CrudRepository<Team, String> {

  List<Team> findTeamsByAgeGroupEquals(AgeGroup ageGroup);

}
