package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "leagueConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueConfiguration {

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;
  private int numberOfDivisions = 1;
  private AgeGroup ageGroup;

  @ElementCollection
  private List<DivisionConfiguration> divisions = new ArrayList<>();

  public LeagueConfiguration() {}

  public LeagueConfiguration(AgeGroup ageGroup, DivisionConfiguration... divisionConfig) {
    this.ageGroup = ageGroup;
    divisions.addAll(Arrays.asList(divisionConfig));
    this.numberOfDivisions = divisionConfig.length;
  }

  public int getNumberOfDivisions() {
    return numberOfDivisions;
  }

  public AgeGroup getAgeGroup() {
    return ageGroup;
  }

  public List<DivisionConfiguration> getDivisions() {
    return divisions;
  }

  public boolean isHomeAndAway(int division) {
    for (DivisionConfiguration divisionConfiguration : divisions) {
      if (divisionConfiguration.getDivisionNumber() == division) {
        return divisionConfiguration.isHomeAndAway();
      }
    }
    return false;
  }

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

}
