package com.redspark.nycl.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    static Date getNext(Date startingFrom, Integer day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(startingFrom);
        while (cal.get(Calendar.DAY_OF_WEEK) != day) {
            cal.add(Calendar.DATE, 1);
        }
        return cal.getTime();
    }
}
