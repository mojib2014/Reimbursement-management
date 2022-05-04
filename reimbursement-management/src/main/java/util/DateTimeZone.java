package util;


import java.sql.Timestamp;
import java.time.ZonedDateTime;

public class DateTimeZone {
    public static Timestamp getDateTimeZone() {
        // Getting local time/timezone
        ZonedDateTime now = ZonedDateTime.now();
        return Timestamp.valueOf(now.toLocalDateTime());
    }
}

