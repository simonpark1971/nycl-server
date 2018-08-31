package com.redspark.nycl.service.impl.postgresql;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Team;
import com.redspark.nycl.persistence.PostgresqlClubRepository;
import com.redspark.nycl.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class PostgresqlClubService implements ClubService {

  @Autowired
  private PostgresqlClubRepository clubRepository;

  @Override
  public void store(Club club) {

    if(club.getId() == null) {
      Club storedClub = getClub(club.getClubName());
      if (null != storedClub) {
        club.setId(storedClub.getId());
      }
    }

    // because we're ignoring the cyclic nature for json
    for (Team team : club.getClubTeams()) {
      team.setClub(club);
    }

    if (club.getMainContact().getUsername() == null || club.getMainContact().getUsername().isEmpty()) {
      club.getMainContact().setUsername(club.getMainContact().getContactName().replaceAll("\\s+","."));
    }
    clubRepository.save(club);
  }

  @Override
  public Club getMainContactsClub(String contactName) {
    return clubRepository.findByMainContactUsername(contactName);
  }

  @Override
  public Club getClub(String clubName) {
    return clubRepository.findClubByClubName(clubName);
  }

  @Override
  public List<Club> getClubList() {

    return StreamSupport.stream(clubRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  @Override
  public void updateClub(String clubName, Club club) {
    clubRepository.save(club);
  }

  @Override
  public void markClubDeleted(String clubName) {

  }

  @Override
  public void updateCupCompetitions(Club club) {

  }

  @Override
  public void markTeamAsDeleted(Club club) {

  }

  @Override
  public void removeAllTeams() {

  }

  @Override
  public void removeDuplicates() {

  }

  @Override
  public void registerNewClub(String myClub, String mainContactName, String emailAddress) {

  }
}
