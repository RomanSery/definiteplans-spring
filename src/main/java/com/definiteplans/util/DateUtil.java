package com.definiteplans.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;


public class DateUtil {

    private static final DateTimeFormatter simpleDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");

    public static final ZoneId defaultTimeZone = ZoneId.of("America/New_York");

    public static LocalDateTime now() {
        return LocalDateTime.now(defaultTimeZone);
    }

    public static boolean isInThePast(LocalDateTime dt) {
        return dt.isBefore(now());
    }

    public static String printDateTime(LocalDateTime dt, ZoneId timezone) {
        if(dt == null) {
            return "";
        }
        if (timezone != null) {
            return fullFormatter.format(dt.atZone(defaultTimeZone).withZoneSameInstant(timezone));
        }
        return fullFormatter.format(dt.atZone(defaultTimeZone));
    }


    public static int getHoursBetween(LocalDateTime d1, LocalDateTime d2) {
        ZonedDateTime dt1 = ZonedDateTime.of(d1, defaultTimeZone);
        ZonedDateTime dt2 = ZonedDateTime.of(d2, defaultTimeZone);
        return (int) Math.abs(ChronoUnit.HOURS.between(dt1, dt2));
    }

    public static int getMinutesBetween(LocalDateTime d1, LocalDateTime d2) {
        ZonedDateTime dt1 = ZonedDateTime.of(d1, defaultTimeZone);
        ZonedDateTime dt2 = ZonedDateTime.of(d2, defaultTimeZone);
        return (int) Math.abs(ChronoUnit.MINUTES.between(dt1, dt2));
    }


    public static String getTimeDifferenceDescription(LocalDateTime dt1) {

        LocalDateTime dt2 = now();
        long numSecs = ChronoUnit.SECONDS.between(dt1, dt2);
        long numMins = ChronoUnit.MINUTES.between(dt1, dt2);
        long numHours = ChronoUnit.HOURS.between(dt1, dt2);
        long numDays = ChronoUnit.DAYS.between(dt1, dt2);
        long numWeeks = ChronoUnit.WEEKS.between(dt1, dt2);
        long numMonths = ChronoUnit.MONTHS.between(dt1, dt2);
        if(numSecs < 0) numSecs = 1;

        if(numMonths > 0)
            return "more than a month ago";
        else if(numWeeks > 0)
            return "more than a week ago";
        else if(numDays > 0)
            return "a few days ago";
        else if(numHours > 0)
            return "today";
        else if(numMins > 0)
            return "a few hours ago";
        else
            return "";
    }

    public static LocalDate parseDate(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        try {
            return LocalDate.parse(str, simpleDateFormatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static String printDate(LocalDate d) {
        return d == null ? "" : simpleDateFormatter.format(d);
    }

    public static String printISODateTime(LocalDateTime dt) {
        return dt != null ? DateTimeFormatter.ISO_DATE_TIME.format(dt.atZone(defaultTimeZone)) : "";
    }

    public static boolean isEligible(LocalDate dob) {
        return getAge(dob) >= 18;
    }

    public static int getAge(LocalDate dob) {
        if(dob == null) {
            return 0;
        }
        return (int) Math.abs(ChronoUnit.YEARS.between(dob, LocalDate.now()));
    }
}
