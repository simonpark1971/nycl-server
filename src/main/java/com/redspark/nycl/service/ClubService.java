package com.redspark.nycl.service;

import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Team;

import java.util.List;

public interface ClubService {

  void store(Club club);

  Club getClub(String clubName);

  List<Club> getClubList();

  void updateClub(String clubName, Club club);

  void markClubDeleted(String clubName);

  void updateCupCompetitions(Club club);

  void markTeamAsDeleted(Club club);

  void removeAllTeams();

  void removeDuplicates();

  void registerNewClub(String myClub, String mainContactName, String emailAddress);

  Club getMainContactsClub(String contactName);
}
