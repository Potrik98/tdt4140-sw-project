package tdt4140.gr1809.app.core.util;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilsTest {
    @Test
    public void testLocalDateTimesAreWithinAPeriodOfEachOther() {
        final LocalDateTime startTime = LocalDateTime.now();
        final LocalDateTime endTime = startTime.plusDays(10);
        final LocalDateTime someTimeInBetween = endTime.minusDays(5);
        final Period timePeriod = Period.ofDays(7);

        assertThat(TimeUtils.localDateTimesAreWithinAPeriodOfEachOther(
                startTime, endTime, timePeriod)).isFalse();
        assertThat(TimeUtils.localDateTimesAreWithinAPeriodOfEachOther(
                startTime, someTimeInBetween, timePeriod)).isTrue();
    }

    @Test
    public void testAverageLocalDateTime() {
        final LocalDateTime startTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        final LocalDateTime endTime = startTime.plusHours(2);
        final LocalDateTime timeInTheMiddle = startTime.plusHours(1);
        assertThat(TimeUtils.averageLocalDateTime(ImmutableList.of(
                startTime, endTime
        ))).isEqualTo(timeInTheMiddle);
    }
}
