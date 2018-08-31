package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.Club;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgresqlClubRepository extends CrudRepository<Club, String> {

  Club findClubByClubName(String clubName);

  Club findByMainContactUsername(String contactName);

  List<Club> findClubsByEnterU11Cup(boolean entering);

  List<Club> findClubsByEnterU12Cup(boolean entering);

  List<Club> findClubsByEnterU14Cup(boolean entering);

}
