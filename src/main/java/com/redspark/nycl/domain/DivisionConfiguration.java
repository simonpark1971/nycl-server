package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "divisionConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionConfiguration {

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;
  private int divisionNumber;
  private boolean homeAndAway;
  private AgeGroup ageGroup;

  public DivisionConfiguration() {}
  /**
   *
   * @param divNum Which division is being added, division 1, 2 etc
   * @param homeAndAway Are the teams playing home AND away
   * @param ageGroup
   */
  public DivisionConfiguration(int divNum, boolean homeAndAway, AgeGroup ageGroup) {

    this.divisionNumber = divNum;
    this.homeAndAway = homeAndAway;
    this.ageGroup = ageGroup;
  }

  public int getDivisionNumber() {
    return divisionNumber;
  }
  public boolean isHomeAndAway() {
    return homeAndAway;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
}
