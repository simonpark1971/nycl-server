package com.redspark.nycl.util;

import com.redspark.nycl.domain.AgeGroup;

/**
 * Created by simonpark on 07/03/2017.
 */
public class DomainUtil {

    public static final AgeGroup getAgeGroup(String ageGroup) {

        if (ageGroup.contains("Under 10 Pairs"))
            return AgeGroup.Under10;
        if (ageGroup.contains("Under 11 Pairs"))
            return AgeGroup.Under11;
        if (ageGroup.contains("Under 12 11-a-side"))
            return AgeGroup.Under12;
        if (ageGroup.contains("Under 12 8-a-side"))
            return AgeGroup.Under13_8S;
        if (ageGroup.contains("Under 13"))
            return AgeGroup.Under13;
        if (ageGroup.contains("Under 14 11-a-side"))
            return AgeGroup.Under14;
        if (ageGroup.contains("Under 14 8-a-side"))
            return AgeGroup.Under15_8S;
        if (ageGroup.contains("Under 15"))
            return AgeGroup.Under15;

        else return AgeGroup.GIRLS;
    }


    public static final Integer getDaysInt(String dayName) {
        if(dayName.contains("Sunday"))
            return 1;
        if(dayName.contains("Monday"))
            return 2;
        if(dayName.contains("Tuesday"))
            return 3;
        if(dayName.contains("Wednesday"))
            return 4;
        if(dayName.contains("Thursday"))
            return 5;
        if(dayName.contains("Friday"))
            return 6;
        if(dayName.contains("Saturday"))
            return 7;
        return -1;
    }
}
