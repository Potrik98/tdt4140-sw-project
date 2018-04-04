package tdt4140.gr1809.app.core.util;

import java.time.LocalDateTime;
import java.time.Period;

public class TimeUtils {
    public static boolean localDateTimesAreWithinAPeriodOfEachOther(final LocalDateTime a,
                                                                      final LocalDateTime b,
                                                                      final Period period) {
        return a.isBefore(b.plus(period)) && a.isAfter(b.minus(period));
    }
}
