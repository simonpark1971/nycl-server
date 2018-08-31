package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgresqlTeamRepository extends CrudRepository<Team, String> {

  List<Team> findTeamsByAgeGroup(AgeGroup age);

}
