package com.redspark.nycl.service.impl;

import com.redspark.nycl.domain.AgeGroup;
import com.redspark.nycl.domain.Club;
import com.redspark.nycl.domain.Contact;
import com.redspark.nycl.domain.Team;
import com.redspark.nycl.service.ClubService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class PostgresqlClubServiceTest {

    @Autowired
    ClubService clubService;

    @Test
    public void getMainContactsClub() {
        Club c = new Club();
        Contact mainContact = c.getMainContact();

        assertEquals("Main contact name not correct", mainContact.getContactName(), "Fred Bloggs");
        assertEquals("Main contact email not correct", mainContact.getContactEmail(), "fred.bloggs@mail.com");
        assertEquals("Main contact home phone not correct", mainContact.getContactHomePhone(), "0123456789");
        assertEquals("Main contact mobile phone not correct", mainContact.getContactMobilePhone(), "+44123456789");
        assertEquals("Main contact address not correct", mainContact.getContactAddress(), "The House, The Street, The Town, The City, PostCode");
        assertEquals("Main contact post code not correct", mainContact.getContactPostcode(), "P05 TCODE");
        assertEquals("Main contact position not correct", mainContact.getContactPosition(), "Secretary");
    }

    @Test
    public void addTeam() {
        clubService.addTeam(new Team());
    }

    @Test
    public void updateCupEntries() {
        clubService.updateCupEntries(new Club());
    }

    @Test
    public void getClub() {
        clubService.getClub("Test Club");
    }

    @Test
    public void getClubList() {
        clubService.getClubList();
    }

    @Test
    public void updateClub() {
        clubService.updateClub("Test Club", new Club());
    }

    @Test
    public void markClubDeleted() {
        clubService.markClubDeleted("Test Club");
    }

    @Test
    public void markTeamAsDeleted() {
        clubService.deleteTeam("Test Club");
    }
}
