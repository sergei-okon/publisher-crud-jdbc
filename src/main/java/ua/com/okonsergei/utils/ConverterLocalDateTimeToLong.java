package ua.com.okonsergei.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public class ConverterLocalDateTimeToLong {

    public static Long convertLocalDateTimeToLong(LocalDateTime dataLocalTime) {
        return dataLocalTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static LocalDateTime convertLongToLocalDateTime(Long dataLong) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(dataLong),
                TimeZone.getDefault().toZoneId());
    }
}
