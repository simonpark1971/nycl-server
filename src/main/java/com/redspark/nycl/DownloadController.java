package com.redspark.nycl;

import com.redspark.nycl.domain.*;
import com.redspark.nycl.service.ClubService;
import com.redspark.nycl.service.SeasonConfigurationService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by simonpark on 26/09/2016.
 */
@RestController
public class DownloadController {

  @Autowired
  private ClubService clubService;

  @Autowired
  private SeasonConfigurationService seasonConfigurationService;

  @GetMapping(path = "/download/clubs")
  @CrossOrigin(origins = "*")
  public ResponseEntity<Resource> downloadClubDetails() throws IOException {
    return getResourceResponseEntity();
  }

  @GetMapping(path = "/download/teams")
  @CrossOrigin(origins = "*")
  public ResponseEntity<Resource> downloadTeamDetails() throws IOException {

    XSSFWorkbook workbook = new XSSFWorkbook();
    for (AgeGroup ageGroup : AgeGroup.values()) {
      XSSFSheet sheet = workbook.createSheet(ageGroup.toString());
      League league = seasonConfigurationService.getLeague("2018", ageGroup);
      List<Team> teams = new ArrayList<>();
      for (Division division : league.getDivisions()) {
        teams.addAll(division.getTeams());
      }
      int rowCounter = 0;
      for (Team team : teams) {
        Row row = sheet.createRow(rowCounter++);
        Cell cell = row.createCell(0);
        cell.setCellValue(team.clubName + " " + team.getName());
      }
    }
    FileOutputStream outputStream = new FileOutputStream("./me.xlsx");
    workbook.write(outputStream);
    workbook.close();


    File file = new File("./me.xlsx");
    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(file.length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(resource);
  }

  @GetMapping(path = "/download/contacts")
  @CrossOrigin(origins = "*")
  public ResponseEntity<Resource> downloadContacts() throws IOException {

    List<Club> clubs = clubService.getClubList();
    XSSFWorkbook workbook = new XSSFWorkbook();
    mainContacts(workbook, clubs);

    byLeague(workbook, clubs);

    byClub(workbook, clubs);

    FileOutputStream outputStream = new FileOutputStream("./me.xlsx");
    workbook.write(outputStream);
    workbook.close();

    File file = new File("./me.xlsx");
    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(file.length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(resource);
  }

  @GetMapping(path = "/download/fixtures")
  @CrossOrigin(origins = "*")
  public ResponseEntity<Resource> downloadFixtures() throws IOException {

    Season season = seasonConfigurationService.createSeason("2018");
    seasonConfigurationService.generateFixtures("2018");

    List<Fixture> fixtures = season.getFixtures();

    XSSFWorkbook workbook = new XSSFWorkbook();
    allFixtureSheet(fixtures, workbook);

    byLeague(season, workbook);

    byClub(season, workbook);

    FileOutputStream outputStream = new FileOutputStream("./me.xlsx");
    workbook.write(outputStream);
    workbook.close();

    File file = new File("./me.xlsx");
    InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");

    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(file.length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(resource);
  }


  private void mainContacts(XSSFWorkbook workbook, List<Club> clubs) {
    XSSFSheet sheet = workbook.createSheet("Main Contacts");
    int rowCounter = 0;
    for (Club club : clubs) {
      Row row = sheet.createRow(rowCounter++);
      Cell dateCell = row.createCell(0);
      dateCell.setCellValue(club.getClubName());

      Cell mainContactName = row.createCell(1);
      mainContactName.setCellValue(club.getMainContact().getContactName());

      Cell mainContactHomePhone = row.createCell(2);
      mainContactHomePhone.setCellValue(club.getMainContact().getContactHomePhone());

      Cell mobuilePhone = row.createCell(3);
      mobuilePhone.setCellValue(club.getMainContact().getContactMobilePhone());

      Cell emailAddress = row.createCell(4);
      emailAddress.setCellValue(club.getMainContact().getContactEmail());
    }
  }
  private void byLeague(XSSFWorkbook workbook, List<Club> clubs) {

    for (AgeGroup ageGroup : AgeGroup.values()) {
      int rowCounter = 0;
      XSSFSheet sheet = workbook.createSheet(ageGroup.toString());

      List<Team> teamsInAgeAgroup = clubs.stream()
        .flatMap(club -> club.getClubTeams().stream())
        .collect(Collectors.toList())
          .stream().filter(team -> team.getAgeGroup().equals(ageGroup)).collect(Collectors.toList());

      for (Team team : teamsInAgeAgroup) {
        Row row = sheet.createRow(rowCounter++);
        Cell dateCell = row.createCell(0);
        dateCell.setCellValue(team.getClubName());

        Cell mainContactName = row.createCell(1);
        mainContactName.setCellValue(team.getPrimaryContact().getContactName());

        Cell mainContactHomePhone = row.createCell(2);
        mainContactHomePhone.setCellValue(team.getPrimaryContact().getContactHomePhone());

        Cell mobuilePhone = row.createCell(3);
        mobuilePhone.setCellValue(team.getPrimaryContact().getContactMobilePhone());

        Cell emailAddress = row.createCell(4);
        emailAddress.setCellValue(team.getPrimaryContact().getContactEmail());

        Cell secondContactName = row.createCell(5);
        secondContactName.setCellValue(team.getSecondaryContact().getContactName());

        Cell secondContactHomePhone = row.createCell(6);
        secondContactHomePhone.setCellValue(team.getSecondaryContact().getContactHomePhone());

        Cell secondContactMobuilePhone = row.createCell(7);
        secondContactMobuilePhone.setCellValue(team.getSecondaryContact().getContactMobilePhone());

        Cell secondContactEmailAddress = row.createCell(8);
        secondContactEmailAddress.setCellValue(team.getSecondaryContact().getContactEmail());
      }
    }
  }

  private void byClub(XSSFWorkbook workbook, List<Club> clubs) {

    for (Club club : clubs) {
      int rowCounter = 0;
      XSSFSheet sheet = workbook.createSheet(club.getClubName());


      Row row = sheet.createRow(rowCounter++);
      Cell dateCell = row.createCell(0);
      dateCell.setCellValue("Main Contact");

      Cell mainContactName = row.createCell(1);
      mainContactName.setCellValue(club.getMainContact().getContactName());

      Cell mainContactHomePhone = row.createCell(2);
      mainContactHomePhone.setCellValue(club.getMainContact().getContactHomePhone());

      Cell mobuilePhone = row.createCell(3);
      mobuilePhone.setCellValue(club.getMainContact().getContactMobilePhone());

      Cell emailAddress = row.createCell(4);
      emailAddress.setCellValue(club.getMainContact().getContactEmail());

      if (club.getFixturesContact() != null ) {
        row = sheet.createRow(rowCounter++);

        dateCell = row.createCell(0);
        dateCell.setCellValue("Fixtures Contact");

        mainContactName = row.createCell(1);
        mainContactName.setCellValue(club.getFixturesContact().getContactName());

        mainContactHomePhone = row.createCell(2);
        mainContactHomePhone.setCellValue(club.getFixturesContact().getContactHomePhone());

        mobuilePhone = row.createCell(3);
        mobuilePhone.setCellValue(club.getFixturesContact().getContactMobilePhone());

        emailAddress = row.createCell(4);
        emailAddress.setCellValue(club.getFixturesContact().getContactEmail());
      }

      for (Team team : club.getClubTeams()) {
        row = sheet.createRow(rowCounter++);
        dateCell = row.createCell(0);
        dateCell.setCellValue(team.getAgeGroup().toString());

        mainContactName = row.createCell(1);
        mainContactName.setCellValue(team.getPrimaryContact().getContactName());

        mainContactHomePhone = row.createCell(2);
        mainContactHomePhone.setCellValue(team.getPrimaryContact().getContactHomePhone());

        mobuilePhone = row.createCell(3);
        mobuilePhone.setCellValue(team.getPrimaryContact().getContactMobilePhone());

        emailAddress = row.createCell(4);
        emailAddress.setCellValue(team.getPrimaryContact().getContactEmail());

        Cell secondContactName = row.createCell(5);
        secondContactName.setCellValue(team.getSecondaryContact().getContactName());

        Cell secondContactHomePhone = row.createCell(6);
        secondContactHomePhone.setCellValue(team.getSecondaryContact().getContactHomePhone());

        Cell secondContactMobuilePhone = row.createCell(7);
        secondContactMobuilePhone.setCellValue(team.getSecondaryContact().getContactMobilePhone());

        Cell secondContactEmailAddress = row.createCell(8);
        secondContactEmailAddress.setCellValue(team.getSecondaryContact().getContactEmail());
      }
    }
  }

  private void byClub(Season season, XSSFWorkbook workbook) {
    List<Club> clubs = clubService.getClubList();
    for (Club club : clubs) {
      XSSFSheet sheet = workbook.createSheet(club.getClubName());
      int rowCounter = 0;
      Set<Fixture> clubFixtures = season.getFixtures().stream()
        .filter(f -> f.getHomeTeam().getClub().getClubName().equals(club.getClubName()) || f.getAwayTeam().getClub().getClubName().equals(club.getClubName()))
        .collect(Collectors.toSet());
      for (Fixture clubFixture : clubFixtures) {
        writeFixture(sheet, rowCounter, clubFixture);
        rowCounter++;
      }
    }
  }

  private void byLeague(Season season, XSSFWorkbook workbook) {
    List<League> leagues = season.getLeagues();
    for (League league : leagues) {
      XSSFSheet sheet = workbook.createSheet(league.getAgeGroup().toString());
      int rowCounter = 0;

      for (Division div : league.getDivisions()) {
        for (Fixture fixture : div.getFixtures()) {
          rowCounter = writeFixture(sheet, rowCounter, fixture);
        }
      }
    }
  }

  private void allFixtureSheet(List<Fixture> fixtures, XSSFWorkbook workbook) {
    XSSFSheet sheet = workbook.createSheet("All Fixtures");
    int rowCounter = 0;

    for (Fixture fixture : fixtures) {
      rowCounter = writeFixture(sheet, rowCounter, fixture);
    }
  }

  private int writeFixture(XSSFSheet sheet, int rowCounter, Fixture fixture) {
    Row row = sheet.createRow(rowCounter++);
    Cell dateCell = row.createCell(0);
    dateCell.setCellValue(fixture.getFormattedFixtureDate());

    Cell homeTeamClubCell = row.createCell(1);
    homeTeamClubCell.setCellValue(fixture.getHomeTeam().getClubName());

    Cell homeTeamCell = row.createCell(2);
    homeTeamCell.setCellValue("(" + fixture.getHomeTeam().getName() + ")");

    Cell homeTeamAgeGroup = row.createCell(3);
    homeTeamAgeGroup.setCellValue(fixture.getHomeTeam().getAgeGroup().toString());

    Cell vCell = row.createCell(4);
    vCell.setCellValue("V");

    Cell awayTeamClubCell = row.createCell(5);
    awayTeamClubCell.setCellValue(fixture.getAwayTeam().getClubName());

    Cell awayTeamCell = row.createCell(6);
    awayTeamCell.setCellValue("(" + fixture.getAwayTeam().getName() + ")");

    Cell awayTeamAgeGroup = row.createCell(7);
    awayTeamAgeGroup.setCellValue(fixture.getAwayTeam().getAgeGroup().toString());
    return rowCounter;
  }


  private ResponseEntity<Resource> getResourceResponseEntity() throws IOException {
    StringWriter out = createCSVFile(new String[] {"Club", "Main Contact", "Email", "Home Phone",
      "Mobile Phone"}, clubService.getClubList());
    String returnString = out.getBuffer().toString();

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
    headers.add("Pragma", "no-cache");
    headers.add("Expires", "0");
    return ResponseEntity.ok()
      .headers(headers)
      .contentLength(out.getBuffer().length())
      .contentType(MediaType.parseMediaType("application/octet-stream"))
      .body(new InputStreamResource(new ByteArrayInputStream(returnString.getBytes())));
  }

  public StringWriter createCSVFile(String [] headers, List<Club> clubs) throws IOException {
    StringWriter out = new StringWriter();
    try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
      for (Club club : clubs) {
        printer.printRecord(club.getClubName(), club.getMainContact().getContactName(),
          club.getMainContact().getContactEmail(), club.getMainContact().getContactHomePhone(),
          club.getMainContact().getContactMobilePhone());
      }
    }
    return out;
  }
}
