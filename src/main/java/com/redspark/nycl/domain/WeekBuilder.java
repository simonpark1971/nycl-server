package com.redspark.nycl.domain;

import com.redspark.nycl.FixtureController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by simonpark on 10/12/2015.
 */
public class WeekBuilder {

  private static final Logger LOGGER = LoggerFactory.getLogger(WeekBuilder.class);

    public static final List<Week> buildWeeks(Date start, Date end, Week... holiday) {
        List<Week> weeks = new ArrayList<Week>();

        Date current = start;
        int weekNumber = 1;
        FixtureController.CURRENT_CALENDAR.setTime(current);
        while (current.before(end)) {
            FixtureController.CURRENT_CALENDAR.add(Calendar.HOUR, 6 * 24);
            Week nextWeek = new Week(current, FixtureController.CURRENT_CALENDAR.getTime(), weekNumber);
            if(isHoliday(nextWeek, holiday)) {
                LOGGER.info("Skipping holiday week: " + nextWeek);
            } else {
                weeks.add(nextWeek);
                weekNumber++;
            }
            FixtureController.CURRENT_CALENDAR.add(Calendar.HOUR, 24);
            current = FixtureController.CURRENT_CALENDAR.getTime();
        }
        return weeks;
    }

    private static boolean isHoliday(Week nextWeek, Week[] holiday) {
        for (Week week : holiday) {
            if(week.start.equals(nextWeek.start) && week.end.equals(nextWeek.end)) {
                return true;
            }
        }
        return false;
    }
}
