package com.redspark.nycl.domain;

import com.redspark.nycl.FixtureController;
import com.redspark.nycl.service.impl.postgresql.PostgresqlSeasonConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by simonpark on 10/12/2015.
 */
public class Week {

    private static final Calendar CALENDAR = new GregorianCalendar();
    private static final Logger LOGGER = LoggerFactory.getLogger(Week.class);

    Date start, end;
    int weekNumber;

    public Week(Date start, Date end, int weekNumber) {
        this.start = start;
        this.end = end;
        this.weekNumber = weekNumber;
    }

    public Week(Date start, Date end) {
        this.start = start;
        this.end = end;
    }


    public boolean inThisWeek(Date date) {
        if(null == date) {
            return false;
        } else {
            if(!date.before(start) && !date.after(end)){
               LOGGER.info("" + date + " is in week " + this);
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object week) {
        return ((Week)week).weekNumber == this.weekNumber;
    }

    public String toString() {
        return "com.nycl.domain.Week " + weekNumber + " starts on "
          + PostgresqlSeasonConfigurationService.DATE_FORMAT.format(start)
          + " and ends on "
          + PostgresqlSeasonConfigurationService.DATE_FORMAT.format(end);
    }

    public List<Date> getDates() {
        List<Date> dates = new ArrayList<>();
        Date current = start;
        while (!current.after(end)) {
            FixtureController.CURRENT_CALENDAR.setTime(current);
            dates.add(new Date(FixtureController.CURRENT_CALENDAR.getTimeInMillis()));
            FixtureController.CURRENT_CALENDAR.add(Calendar.HOUR, 24);
            current = FixtureController.CURRENT_CALENDAR.getTime();
        }
        return dates;
    }

    public static boolean withinNDays(Date fixtureDate, Date compareTo, int i) {
        long diff = TimeUnit.DAYS.convert(fixtureDate.getTime() - compareTo.getTime(), TimeUnit.MILLISECONDS);
        boolean isWithin = true;
        diff = diff < 0 ? diff*-1 : diff;
        isWithin = diff <= i;
        return isWithin;
    }
}
