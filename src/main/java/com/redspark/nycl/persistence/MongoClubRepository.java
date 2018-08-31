package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoClubRepository extends MongoRepository<Club, String> {

  List<Club> findClubsByClubName(String clubName);
}
