package tdt4140.gr1809.app.core.value;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class NumerableTest {
    @Test
    public void testNumerableLocalDateTime() {
        final LocalDateTimeNumberConverter localDateTimeNumberConverter =
                new LocalDateTimeNumberConverter();
        final Numerable.NumerableBuilder<LocalDateTime> numerableBuilder =
                new Numerable.NumerableBuilder<>(localDateTimeNumberConverter);
        final LocalDateTime localDateTime = LocalDateTime.now();

        final Numerable<LocalDateTime> localDateTimeNumerable = numerableBuilder.numerableOfValue(localDateTime);
        assertThat(numerableBuilder.numerableOfDouble(localDateTimeNumerable.doubleValue())
                .getValue()).isEqualToIgnoringSeconds(localDateTime);
        assertThat(numerableBuilder.numerableOfFloat(localDateTimeNumerable.floatValue())
                .getValue()).isEqualToIgnoringMinutes(localDateTime);
        assertThat(numerableBuilder.numerableOfLong(localDateTimeNumerable.longValue())
                .getValue()).isEqualToIgnoringNanos(localDateTime);
        assertThat(numerableBuilder.numerableOfInt(localDateTimeNumerable.intValue())
                .getValue()).isEqualToIgnoringNanos(localDateTime);
    }
}
