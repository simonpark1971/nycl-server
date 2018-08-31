package com.redspark.nycl.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by simonpark on 10/12/2015.
 */
public class Fixture {


  public Date getFixtureDate() {
    return fixtureDate;
  }

  public String getFormattedFixtureDate() {
    return null == fixtureDate ? "" : new SimpleDateFormat("EEE dd MMM YYYY").format(fixtureDate);
  }

  public void setFixtureDate(Date fixtureDate) {
    this.fixtureDate = fixtureDate;
  }

  public Team getHomeTeam() {
    return homeTeam;
  }

  public boolean isValid() {
    return homeTeam != null && awayTeam != null;
  }

  public boolean isForHomeOrAway(Team t) {
    return t != null && this.isValid() && (homeTeam.equals(t) || awayTeam.equals(t));
  }

  public boolean isForHomeOrAway(Club club) {
    return club != null &&
      this.homeTeam != null &&
      this.homeTeam.getClub() != null &&
      this.awayTeam != null &&
      this.awayTeam.getClub() != null &&
      this.isValid() && (homeTeam.getClub().equals(club) || awayTeam.getClub().equals(club));
  }


  public Team getAwayTeam() {
    return awayTeam;
  }

  private Date fixtureDate;
  private Team homeTeam;
  private Team awayTeam;

  public Fixture(Team homeTeam, Team awayTeam) {
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }

  public String toString() {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    return this.homeTeam + "(H) versus " + this.awayTeam + "(A)," + (null == fixtureDate ? "" : formatter.format(fixtureDate));
  }

  public Date getDate() {
    return fixtureDate;
  }

  public boolean isFixtureOnDate(Date dt) {
    return dt != null && this.fixtureDate != null && this.fixtureDate.equals(dt);
  }

  public boolean isClubAgeGroupInvolvedInFixture(Club club, AgeGroup[] ageGroups) {
    return isOfAgeGroupsIsInvolvedInFixture(club, ageGroups);
  }

  private boolean isOfAgeGroupsIsInvolvedInFixture(Club club, AgeGroup[] ageGroups) {
    return isHomeClubAgeGroupInvolved(club, ageGroups) || isAwayClubAgeGroupInvolved(club, ageGroups);
  }

  private boolean isHomeClubAgeGroupInvolved(Club club, AgeGroup[] ageGroups) {
    for (AgeGroup ageGroup : ageGroups) {
      if (this.homeTeam.getClub().equals(club) && this.homeTeam.getAgeGroup().equals(ageGroup))
        return true;
    }
    return false;
  }

  private boolean isAwayClubAgeGroupInvolved(Club club, AgeGroup[] ageGroups) {
    for (AgeGroup ageGroup : ageGroups) {
      if (this.awayTeam.getClub().equals(club) && this.awayTeam.getAgeGroup().equals(ageGroup))
        return true;
    }
    return false;
  }

  public Set<Date> findDates(Week week) {
    return this.homeTeam.getAllPossibleDates(week);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Fixture fixture = (Fixture) o;

    if (fixtureDate != null ? !fixtureDate.equals(fixture.fixtureDate) : fixture.fixtureDate != null) return false;
    if (homeTeam != null ? !homeTeam.equals(fixture.homeTeam) : fixture.homeTeam != null) return false;
    return awayTeam != null ? awayTeam.equals(fixture.awayTeam) : fixture.awayTeam == null;
  }

  @Override
  public int hashCode() {
    int result = fixtureDate != null ? fixtureDate.hashCode() : 0;
    result = 31 * result + (homeTeam != null ? homeTeam.hashCode() : 0);
    result = 31 * result + (awayTeam != null ? awayTeam.hashCode() : 0);
    return result;
  }

  public boolean hasDuplicateClubHomeGame(Club club, Set<Fixture> otherFixtures) {
    return otherFixtures.stream()
      .filter(f -> f.getHomeTeam().equals(this.homeTeam) && f.getDate().equals(this.getDate()))
      .collect(Collectors.toList()).size() > 0;
  }
}
