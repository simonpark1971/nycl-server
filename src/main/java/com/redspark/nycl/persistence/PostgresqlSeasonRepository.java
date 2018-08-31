package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.Season;
import org.springframework.data.repository.CrudRepository;

public interface PostgresqlSeasonRepository extends CrudRepository<Season, String> {

  Season findByName(String name);

}
