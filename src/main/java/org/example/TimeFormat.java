package org.example;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeFormat {
    public static String timeFormatter(String currentTime) {
        long timestamp = Long.parseLong(currentTime); // Your timestamp

        // Convert milliseconds since Unix epoch to Instant
        Instant instant = Instant.ofEpochMilli(timestamp);

        // Convert Instant to LocalDateTime (default timezone is UTC)
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        // Format LocalDateTime as a string
        String formattedDateTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return formattedDateTime;
    }
}
