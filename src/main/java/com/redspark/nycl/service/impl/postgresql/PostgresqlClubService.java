package com.redspark.nycl.service.impl.postgresql;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Team;
import com.redspark.nycl.persistence.PostgresqlClubRepository;
import com.redspark.nycl.persistence.PostgresqlTeamRepository;
import com.redspark.nycl.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class PostgresqlClubService implements ClubService {

  @Autowired
  private PostgresqlClubRepository clubRepository;

  @Autowired
  private PostgresqlTeamRepository teamRepository;

  @Override
  public void store(Club club) {

    if (club.getId() == null || club.getId().isEmpty()) {
      Club storedClub = getClub(club.getClubName());
      if (null != storedClub) {
        club.setId(storedClub.getId());
      }
    }

    // because we're ignoring the cyclic nature for json
    for (Team team : club.getClubTeams()) {
      team.setClub(club);
    }

    club.getMainContact().setUsername(club.getMainContact().getContactEmail());
    clubRepository.save(club);
  }

  @Override
  public Club getMainContactsClub(String contactName) {
    return clubRepository.findByMainContactUsername(contactName);
  }

  @Override
  public void addTeam(Team team) {
    Club teamsClub = this.getClub(team.getClubName());
    teamsClub.getClubTeams().add(team);
    team.setClub(teamsClub);
    this.updateClub(team.getClubName(), teamsClub);
  }

  @Override
  public void updateCupEntries(Club club) {
  }

  @Override
  public void updateTeamRanking(String clubName, AgeGroup ageGroup, int rank) {
    Club club = this.getClub(clubName);
    if (club != null) {
      for (Team team : club.getClubTeams()) {
        if (team.getAgeGroup() == ageGroup && !team.isDeleted()) {
          team.setRank(rank);
          this.store(club);
          return;
        }
      }
    }
  }

  @Override
  public Club getClubById(String id) {
    return clubRepository.findById(id).get();
  }

  @Override
  public Club getClubByUserName(String username) {
    return clubRepository.findClubByUsername(username);
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
    Club club = this.getClub(clubName);
  }

  @Override
  public void deleteTeam(String teamId) {
    Optional<Team> t = teamRepository.findById(teamId);
    if(t.isPresent()) {
      teamRepository.delete(t.get());
    }
  }
}
