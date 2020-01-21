package com.redspark.nycl.service;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Team;

import java.util.List;

public interface ClubService {

  void store(Club club);

  Club getClub(String clubName);

  List<Club> getClubList();

  void updateClub(String clubName, Club club);

  void markClubDeleted(String clubName);

  void deleteTeam(String teamId);

  Club getMainContactsClub(String contactName);

  void addTeam(Team team);

  void updateCupEntries(Club club);

  void updateTeamRanking(String clubName, AgeGroup ageGroup, int rank);

  Club getClubById(String id);

  Club getClubByUserName(String username);
}
