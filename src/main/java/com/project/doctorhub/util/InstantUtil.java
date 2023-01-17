package com.project.doctorhub.util;

import com.project.doctorhub.schedule.model.DayOfWeek;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class InstantUtil {

    public static Instant getInstantWithoutHour(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(instant.toEpochMilli()) + " 00:00";
        return LocalDateTime.parse(
                        date,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(ZoneId.of("UTC"))
                .toInstant();
    }

    public static List<Instant> getDateFromDayOfWeek(com.project.doctorhub.schedule.model.DayOfWeek day, Instant until, Instant from) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE");
        List<Instant> result = new ArrayList<>();
        Instant temp = from;
        while (temp.isBefore(until)) {
            if (DayOfWeek.valueOf(dateFormat.format(temp.toEpochMilli()).toUpperCase()) == day) {
                result.add(temp);
            }
            temp = temp.plus(1, ChronoUnit.DAYS);
        }
        return result;
    }

    public static Instant getInstantFromSting(String string) {
        return LocalDateTime.parse(
                        string,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(ZoneId.of("UTC"))
                .toInstant();
    }

}
