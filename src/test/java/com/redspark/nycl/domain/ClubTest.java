package com.redspark.nycl.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClubTest {

    @Test
    public void applicationStatusIsOpenForNewInstance() {
        Club club = new Club();

        assertEquals("Application status should be open for new club", club.getApplicationStatus(), "open");
    }

    @Test
    public void deletedStatusIsFalseForNewInstance() {
        Club club = new Club();

        assertEquals("Deleted status should be false for new club", club.deleted, false);
    }

    @Test
    public void newClubIsInNoCupCompetitions() {
        Club club = new Club();

        assertEquals("New club should not be in U11 cup", club.isEnterU11Cup(), false);
        assertEquals("New club should not be in U12 cup", club.isEnterU12Cup(), false);
        assertEquals("New club should not be in U14 cup", club.isEnterU14Cup(), false);
    }

    @Test
    public void newClubShouldHaveOnePitchByDefault() {
        Club club = new Club();

        assertEquals("Club should have one pitch by default", club.pitches, 1);
    }

    @Test
    public void newClubShouldHaveNoTeams() {
        Club club = new Club();

        assertEquals("Club should have no teams after creation", club.getClubTeams().size(), 0);
    }
}
