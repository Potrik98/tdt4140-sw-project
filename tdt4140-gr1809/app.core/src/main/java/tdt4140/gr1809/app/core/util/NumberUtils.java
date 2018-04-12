package tdt4140.gr1809.app.core.util;

public class NumberUtils {
    /**
     * Clamps a value between two bounds
     *
     * @param lowerBound lower bound for the value
     * @param upperBound upper bound for the value
     * @param value the value to clamp to the interval
     * @return clamped value
     */
    public static double clamp(final double lowerBound,
                               final double upperBound,
                               final double value) {
        return Math.max(lowerBound, Math.min(upperBound, value));
    }
}
