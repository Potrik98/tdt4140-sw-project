package tdt4140.gr1809.app.core.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {
    public static boolean localDateTimesAreWithinADurationOfEachOther(final LocalDateTime a,
                                                                      final LocalDateTime b,
                                                                      final Duration duration) {
        return a.isBefore(b.plus(duration)) && a.isAfter(b.minus(duration));
    }
}
