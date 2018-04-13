package tdt4140.gr1809.app.core.util;

import tdt4140.gr1809.app.core.value.LocalDateTimeNumberConverter;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.List;

public class TimeUtils {
    private final static LocalDateTimeNumberConverter localDateTimeNumberConverter
            = new LocalDateTimeNumberConverter();

    public static boolean localDateTimesAreWithinAPeriodOfEachOther(final LocalDateTime a,
                                                                    final LocalDateTime b,
                                                                    final Period period) {
        return a.isBefore(b.plus(period)) && a.isAfter(b.minus(period));
    }

    public static LocalDateTime averageLocalDateTime(final List<LocalDateTime> localDateTimes) {
        return localDateTimeNumberConverter.ofLongValue(
                localDateTimes.stream()
                    .mapToLong(localDateTimeNumberConverter::toLongValue)
                    .sum() / localDateTimes.size());
    }
}
