package com.redspark.nycl.persistence;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.service.ClubService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDBClubRepoTest {

  @Autowired
  private ClubService service;

  @Test
  public void testRegisterNewClub() {
    service.registerNewClub("My Club", "Main Contact Name", "email@address");
  }

  @Test
  public void getClubList() throws Exception {
    List<Club> clubs = service.getClubList();
  }

  @Test
  public void removeAllTeamsWorks() {

    service.removeAllTeams();
  }

  @Test
  public void dedupeWorks() {
    service.removeDuplicates();
  }

}
