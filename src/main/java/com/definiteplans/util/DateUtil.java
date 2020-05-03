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

    public static LocalDateTime getCurrentServerTime() {
        ZoneId zone = ZoneId.of("America/New_York");
        LocalDateTime dt = LocalDateTime.now(zone);
        return dt;
    }


    public static boolean isInThePast(LocalDateTime dt) {
        ZoneId zone = ZoneId.of("America/New_York");
        LocalDateTime dtNow = LocalDateTime.now(zone);
        return dt.isBefore(dtNow);
    }


    public static int getHoursBetween(LocalDateTime d1, LocalDateTime d2) {
        ZoneId zone = ZoneId.of("America/New_York");
        ZonedDateTime dt1 = ZonedDateTime.of(d1, zone);
        ZonedDateTime dt2 = ZonedDateTime.of(d2, zone);
        return (int) Math.abs(ChronoUnit.HOURS.between(dt1, dt2));
    }

    public static boolean isEligible(LocalDate dob) {
        return getAge(dob) >= 18;
    }

    public static int getAge(LocalDate dob) {
        return (int) Math.abs(ChronoUnit.YEARS.between(dob, LocalDate.now()));
    }

    public static String printDateTime(LocalDateTime dt) {
        if(dt == null) {
            return "";
        }
        ZoneId zone = ZoneId.of("America/New_York");
        return fullFormatter.format(dt.atZone(zone));
    }


    public static String printDate(LocalDate d) {
        if(d == null) {
            return "";
        }
        return simpleDateFormatter.format(d);
    }

    public static String printISODateTime(LocalDateTime dt) {
        return dt != null ? DateTimeFormatter.ISO_DATE_TIME.format(dt) : "";
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
}
