package com.n26.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static long toSeconds( LocalDateTime time ) {
        return time.atZone( ZoneId.of( "UTC" ) ).toEpochSecond();
    }
}
