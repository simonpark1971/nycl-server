package com.redspark.nycl.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "seasonConfiguration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeasonConfiguration {

  @ElementCollection
  private Map<AgeGroup, LeagueConfiguration> ageGroupConfigurations = new HashMap();
  private String tag;

  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  public SeasonConfiguration() {}

  public SeasonConfiguration(String tag) {
    this.tag = tag;
    for (AgeGroup ageGroup : AgeGroup.values()) {
      add(new LeagueConfiguration(ageGroup, new DivisionConfiguration(1,
        false, ageGroup)));
    }
  }

  private void add(LeagueConfiguration leagueConfig) {
    this.ageGroupConfigurations.put(leagueConfig.getAgeGroup(), leagueConfig);
  }

  public Map<AgeGroup, LeagueConfiguration> getAgeGroupConfigurations() {
    return ageGroupConfigurations;
  }
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

}
