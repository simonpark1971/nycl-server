package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by simonpark on 10/12/2015.
 */
@Entity
@Table(name = "club")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Club {

  private static final Logger LOGGER = LoggerFactory.getLogger(Club.class);

  @Column(name = "number_of_pitches")
  public int pitches = 1;

  public int getPitches() {
    return pitches;
  }

  public void setPitches(int pitches) {
    this.pitches = pitches;
  }

  @Column(name = "deleted")
  public boolean deleted = false;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Column(name = "username")
  private String username;

  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
  private List<Team> clubTeams = new ArrayList<Team>();

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  @Column(name="club_name", unique = true)
  private String clubName;

  public String getApplicationStatus() {
    return applicationStatus;
  }

  public void setApplicationStatus(String applicationStatus) {
    this.applicationStatus = applicationStatus;
  }

  private String applicationStatus = "open";

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "contactAddress", column = @Column(name = "main_contact_address")),
    @AttributeOverride(name = "contactEmail", column = @Column(name = "main_contact_email")),
    @AttributeOverride(name = "contactName", column = @Column(name = "main_contact_name")),
    @AttributeOverride(name = "contactHomePhone", column = @Column(name = "main_contact_home_phone")),
    @AttributeOverride(name = "contactMobilePhone", column = @Column(name = "main_contact_mobile_phone")),
    @AttributeOverride(name = "contactPostcode", column = @Column(name = "main_contact_postcode")),
    @AttributeOverride(name = "username", column = @Column(name = "main_contact_username")),
    @AttributeOverride(name = "contactType", column = @Column(name = "main_contact_type")),
    @AttributeOverride(name = "contactPosition", column = @Column(name = "main_contact_position"))})
  private Contact mainContact;

  @Column(name="last_update_date")
  private Date lastUpdateDate;

  public boolean isEnterU14Cup() {
    return enterU14Cup;
  }

  public void setEnterU14Cup(boolean enterU14Cup) {
    this.enterU14Cup = enterU14Cup;
  }

  public boolean isEnterU12Cup() {
    return enterU12Cup;
  }

  public void setEnterU12Cup(boolean enterU12Cup) {
    this.enterU12Cup = enterU12Cup;
  }

  public boolean isEnterU11Cup() {
    return enterU11Cup;
  }

  public void setEnterU11Cup(boolean enterU11Cup) {
    this.enterU11Cup = enterU11Cup;
  }

  private boolean enterU14Cup = false;
  private boolean enterU12Cup = false;
  private boolean enterU11Cup = false;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "contactAddress", column = @Column(name = "fixtures_contact_address")),
    @AttributeOverride(name = "contactEmail", column = @Column(name = "fixtures_contact_email")),
    @AttributeOverride(name = "contactName", column = @Column(name = "fixtures_contact_name")),
    @AttributeOverride(name = "contactHomePhone", column = @Column(name = "fixtures_contact_home_phone")),
    @AttributeOverride(name = "contactMobilePhone", column = @Column(name = "fixtures_contact_mobile_phone")),
    @AttributeOverride(name = "contactPostcode", column = @Column(name = "fixtures_contact_postcode")),
    @AttributeOverride(name = "username", column = @Column(name = "fixtures_contact_username")),
    @AttributeOverride(name = "contactType", column = @Column(name = "fixtures_contact_type")),
    @AttributeOverride(name = "contactPosition", column = @Column(name = "fixtures_contact_position"))})
  private Contact fixturesContact;

  private String applicantName;
  private String applicantPosition;

  public Club() {}

  public Club(String clubName, int pitches) {
    this.clubName = clubName;
    this.pitches = pitches;
  }

  public Team getTeam(AgeGroup ageGrp, String teamName) {
    for (Team clubTeam : clubTeams) {
      if(teamName == null) {
        if(clubTeam.getAgeGroup() == ageGrp) {
          LOGGER.info("Team found ->" +clubName + " " + ageGrp.toString() + ", " + teamName);
          return clubTeam;
        }
      } else {
        if(clubTeam.getAgeGroup() == ageGrp && clubTeam.getTeamName() != null && teamName.contains(clubTeam.getTeamName())) {
          LOGGER.info("Team found ->" +clubName + " " + ageGrp.toString() + ", " + teamName);
          return clubTeam;
        }
      }
    }
    LOGGER.warn("No team found ->" +clubName + " " + ageGrp.toString() + ", " + teamName);
    return null;
  }

  public void addToCup(CupCompetition comp) {
    switch (comp) {
      case Under11:
        this.enterU11Cup = true;
        break;
      case Under12:
        this.enterU12Cup = true;
        break;
      case Under14:
        this.enterU14Cup = true;
        break;
      default:
        break;
    }
  }

  public String toString() {
    return clubName;
  }

  public boolean equals(Object other) {
    return this.clubName.equals(((Club)other).clubName);
  }

  public void init() {
    if(clubTeams != null) {
      for (Team team : clubTeams) {
        team.setClub(this);
        team.setClubName(this.clubName);
      }
    } else {
      LOGGER.warn("Club has no teams " + clubName);
    }
  }

  public Club(String clubName, String contactName, String email) {
    this.clubName = clubName;
    mainContact = Contact.createContact(contactName,null, null, null,null,null,
      email, Contact.ContactType.MAIN_CONTACT);
  }

  public void init(List<FixtureRule> rules, Team... teams) {
    for (Team team : teams) {
      clubTeams.add(team);
      team.setRules(rules);
    }
  }

  public void init(List<FixtureRule> rules, List<Team> teams) {
    clubTeams.addAll(teams);
    for (Team team : teams) {
      team.setRules(rules);
    }
  }

  public void removeDeletedTeams() {
    if(this.clubTeams != null)
      this.clubTeams = clubTeams.stream().filter(t -> !t.isDeleted()).collect(Collectors.toList());
  }

  public boolean hasTeam(AgeGroup girls, String teamName) {
    for (Team clubTeam : clubTeams) {
      if(clubTeam.getAgeGroup() == girls && clubTeam.getTeamName() != null && clubTeam.getTeamName().equals(teamName)) {
        return true;
      }
    }
    return false;
  }

  public List<Team> getTeams(AgeGroup ageGrp, int division) {
    if(this.clubTeams != null)
      return clubTeams.stream().filter(t -> t.getAgeGroup() == ageGrp).collect(Collectors.toList());
    return new ArrayList<>();
  }

  public Set<Fixture> validateFixtures(Set<Fixture> fixtures) {
    return fixtures.stream()
      .filter(fixture -> fixture.hasDuplicateClubHomeGame(this, fixtures))
      .collect(Collectors.toSet());
  }

  public boolean isThereAPitchAvailableOn(Set<Fixture> allFixtures, Date dt) {
    return getRemainingHomeClubCapacity(allFixtures, dt) >= 1;
  }

  private int getRemainingHomeClubCapacity(Set<Fixture> allFixtures, Date dt) {
    int homeOnThisDay = getHomeFixtureCountOnDate(dt, allFixtures);
    return this.pitches - homeOnThisDay;
  }

  public int getHomeFixtureCountOnDate(Date dt, Set<Fixture> allFixtures) {
    return allFixtures.stream().filter(f -> isHomeGameOnDate(f, dt))
      .collect(Collectors.toList()).size();
  }

  private boolean isHomeGameOnDate(Fixture f, Date dt) {
    return f.getHomeTeam().getClub().equals(this)
      && f.isFixtureOnDate(dt);
  }

  public boolean adjacentTeamClashes(AgeGroup ageGroup, Date dt, Set<Fixture> allFixtures) {
    switch (ageGroup) {
      case Under10:
        return existingGameFor(dt, allFixtures, AgeGroup.Under11);
      case Under11:
        return existingGameFor(dt, allFixtures, AgeGroup.Under10, AgeGroup.Under12);
      case Under12:
        return existingGameFor(dt, allFixtures, AgeGroup.Under11, AgeGroup.Under13);
      case Under13:
        return existingGameFor(dt, allFixtures, AgeGroup.Under12, AgeGroup.Under14, AgeGroup.Under13_8S);
      case Under14:
        return existingGameFor(dt, allFixtures, AgeGroup.Under13, AgeGroup.Under15, AgeGroup.Under15_8S, AgeGroup.Under13_8S);
      case Under15:
        return existingGameFor(dt, allFixtures, AgeGroup.Under14, AgeGroup.Under15_8S);
        case Under12_8S:
            return existingGameFor(dt, allFixtures, AgeGroup.Under12, AgeGroup.Under13, AgeGroup.Under11, AgeGroup.Under13_8S);
      case Under13_8S:
        return existingGameFor(dt, allFixtures, AgeGroup.Under12, AgeGroup.Under13, AgeGroup.Under14, AgeGroup.Under15_8S);
      case Under15_8S:
        return existingGameFor(dt, allFixtures, AgeGroup.Under14, AgeGroup.Under15);
      default:
        return false;
    }
  }

  boolean existingGameFor(Date dt, Set<Fixture> allFixtures, AgeGroup... ageGroups) {
    return allFixtures.stream().filter(f -> f.isValid() && (f.isClubAgeGroupInvolvedInFixture(this, ageGroups)) && (f.isFixtureOnDate(dt)))
      .collect(Collectors.toList()).size() > 0;
  }

  public void removeAllTeams() {
    this.clubTeams.clear();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<Team> getClubTeams() {
    return clubTeams != null ? clubTeams : new ArrayList<Team>();
  }

  public String getClubName() { return clubName; }

  public void setLastUpdateDate() {
    lastUpdateDate = new Date();
  }

  public Date getLastUpdateDate() {
    return lastUpdateDate;
  }

  public void setLastUpdateDate(Date date) {
    lastUpdateDate = date;
  }

  public Contact getMainContact() {
    return mainContact;
  }

  public void setMainContact(Contact mainContact) {
    this.mainContact = mainContact;
  }

  public Contact getFixturesContact() {
    return fixturesContact;
  }

  public void setFixturesContact(Contact fixturesContact) {
    this.fixturesContact = fixturesContact;
  }

  public String getApplicantName() {
    return applicantName;
  }

  public void setApplicantName(String applicantName) {
    this.applicantName = applicantName;
  }

  public String getApplicantPosition() {
    return applicantPosition;
  }

  public void setApplicantPosition(String applicantPosition) {
    this.applicantPosition = applicantPosition;
  }

}
