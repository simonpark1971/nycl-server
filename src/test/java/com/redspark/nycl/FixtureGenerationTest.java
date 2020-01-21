package com.redspark.nycl;

import com.redspark.nycl.domain.Fixture;
import com.redspark.nycl.domain.Season;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@WebAppConfiguration
@TestPropertySource( locations = "classpath:integration-test.properties")
public class FixtureGenerationTest {

  @Autowired
  SeasonConfigurationService seasonConfigurationService;

  @Test
  public void testGenerate() {
      Season season = seasonConfigurationService.createSeason("2019");
      List<Fixture> fixtures = seasonConfigurationService.generateFixtures("2019");

    System.out.println(season.getFixtures());
  }
}
