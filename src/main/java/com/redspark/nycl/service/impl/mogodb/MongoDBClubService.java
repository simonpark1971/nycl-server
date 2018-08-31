package com.redspark.nycl.service.impl.mogodb;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.persistence.MongoClubRepository;
import com.redspark.nycl.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by simonpark on 03/10/2016.
 */
public class MongoDBClubService implements ClubService {

  @Autowired
  private MongoClubRepository mongoClubRepository;

    @Override
    public void store(Club club) {

      mongoClubRepository.save(club);
    }

    @Override
    public Club getClub(String clubName) {

      List<Club> clubs = getClubList();
      if (clubs.size() > 1) {
        throw new IllegalStateException();
      }
      return clubs.size() == 1 ? clubs.get(0) : null;
    }

    @Override
    public List<Club> getClubList() {
      return mongoClubRepository.findAll();
    }

    @Override
    public void updateClub(String clubName, Club club) {

      mongoClubRepository.save(club);
    }

    @Override
    public void markClubDeleted(String clubName) { }

    @Override
    public void updateCupCompetitions(Club club) {}

    @Override
    public void markTeamAsDeleted(Club club) { }

  @Override
  public void removeAllTeams() {
    List<Club> clubs = this.getClubList();
    for (Club club1 : clubs) {
      club1.removeAllTeams();
      this.store(club1);
    }
  }

  @Override
  public void removeDuplicates() {
      List<Club> clubs = this.getClubList();
      for (Club club1 : clubs) {
        List<Club> clubswithsamename = mongoClubRepository.findClubsByClubName(club1.getClubName());
        int index = 0;
        for (Club club : clubswithsamename) {
          if(index != 0) {
            mongoClubRepository.delete(club);
          }
          index++;
        }
      }
  }

  @Override
  public void registerNewClub(String myClub, String mainContactName, String emailAddress) {
      Club c = new Club(myClub, mainContactName, emailAddress);
      this.store(c);
  }

  @Override
  public Club getMainContactsClub(String contactName) {
    return null;
  }
}
