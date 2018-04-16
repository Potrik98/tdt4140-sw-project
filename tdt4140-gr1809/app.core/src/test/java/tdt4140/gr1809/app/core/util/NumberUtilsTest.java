package tdt4140.gr1809.app.core.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberUtilsTest {
    @Test
    public void testClamp() {
        final double lowerBound = 0.0;
        final double upperBound = 10.0;
        final double below = -5.0;
        final double above = 15.0;
        final double within = 5.0;

        assertThat(NumberUtils.clamp(lowerBound, upperBound, below)).isEqualTo(lowerBound);
        assertThat(NumberUtils.clamp(lowerBound, upperBound, above)).isEqualTo(upperBound);
        assertThat(NumberUtils.clamp(lowerBound, upperBound, within)).isEqualTo(within);
    }
}
