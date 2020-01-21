package com.redspark.nycl.domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class TeamTest {

    @Test
    public void newTeamIsCorrectlySetup() {
        Team t = new Team();

        assertFalse("New team is marked as deleted", t.isDeleted());
        assertFalse("New team can't play mondays", t.isPlayHomeMondays());
        assertFalse("New team can't play tuesdays", t.isPlayHomeTuesdays());
        assertFalse("New team can't play wednesdays", t.isPlayHomeWednesdays());
        assertFalse("New team can't play thursdays", t.isPlayHomeThursdays());
        assertFalse("New team can't play fridays", t.isPlayHomeFridays());
        assertFalse("New team can't play sundays", t.isPlayHomeSundays());
    }

    @Test
    public void newTeamHasNoPreferredHomeDays() {
        Team t = new Team();

        assertFalse("New team is preferred home days", t.hasPreferredHomeDay());
    }
}
