package com.pongo.pongoedu.shared.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatarData(LocalDateTime data) {
        if (data == null) {
            return null;
        }
        return data.format(FORMATTER);
    }

    public static LocalDateTime fazerParse(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(data, FORMATTER);
    }

    public static boolean isFutureDate(LocalDateTime data) {
        return data.isAfter(LocalDateTime.now());
    }

    public static boolean isPastDate(LocalDateTime data) {
        return data.isBefore(LocalDateTime.now());
    }

    public static long getDaysBetween(LocalDateTime start, LocalDateTime end) {
        return java.time.temporal.ChronoUnit.DAYS.between(start, end);
    }
}

