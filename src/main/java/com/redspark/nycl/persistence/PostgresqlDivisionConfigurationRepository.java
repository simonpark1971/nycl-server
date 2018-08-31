package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.DivisionConfiguration;
import com.redspark.nycl.domain.Season;
import org.springframework.data.repository.CrudRepository;

public interface PostgresqlDivisionConfigurationRepository extends CrudRepository<DivisionConfiguration, String> {

}
