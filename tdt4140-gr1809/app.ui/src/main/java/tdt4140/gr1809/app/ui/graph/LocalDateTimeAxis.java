package tdt4140.gr1809.app.ui.graph;

import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.core.value.LocalDateTimeNumberConverter;
import tdt4140.gr1809.app.core.value.NumberConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class LocalDateTimeAxis extends NumerableAxis<LocalDateTime> {
    private static final NumberConverter<LocalDateTime> localDateTimeNumberConverter =
            new LocalDateTimeNumberConverter();

    protected class LocalDateTimeRange extends NumerableAxis.Range {
        final String format;
        protected LocalDateTimeRange(final double lowerBound,
                                     final double upperBound,
                                     final double tickUnit,
                                     final double scale,
                                     final String format) {
            super(lowerBound, upperBound, tickUnit, scale);
            this.format = format;
        }
    }

    /*
     * Default tick unit values.
     * The time axis will try to default to one of these units
     * for the axis intervals.
     */
    private enum TickUnitDefault {
        MINUTES_1(60000, "dd-MMM HH:mm:ss"),
        MINUTES_5(300000, "dd-MMM HH:mm:ss"),
        MINUTES_10(600000, "dd-MMM HH:mm:ss"),
        MINUTES_20(1200000, "dd-MMM HH:mm:ss"),
        MINUTES_30(1800000, "dd-MMM HH:mm"),
        HOURS_1(3600000, "dd-MMM HH:mm"),
        HOURS_2(7200000, "dd-MMM HH:mm"),
        HOURS_3(10800000, "dd-MMM HH:mm"),
        HOURS_6(21600000, "dd-MMM HH"),
        HOURS_12(43200000, "dd-MMM HH"),
        DAYS_1(86400000, "dd-MMM HH"),
        DAYS_2(172800000, "dd-MMM HH"),
        DAYS_3(259200000, "dd-MMM HH"),
        WEEKS_1(604800000, "dd-MMM"),
        WEEKS_2(1209600000, "dd-MMM"),
        MONTHS_1(872640000E1, "dd-MM");
        private final double tickUnit;
        private final String format;
        TickUnitDefault(final double tickUnit, final String format) {
            this.tickUnit = tickUnit;
            this.format = format;
        }
    }

    /*
     * The format used for the tick unit marks
     */
    private final StringProperty tickUnitFormat = new StringPropertyBase("dd-MMM HH:mm") {
        @Override
        public Object getBean() {
            return LocalDateTimeAxis.this;
        }

        @Override
        public String getName() {
            return "tickUnitFormat";
        }
    };

    /**
     * Creates an auto-ranging time axis
     */
    public LocalDateTimeAxis() {
        super(localDateTimeNumberConverter);
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     */
    public LocalDateTimeAxis(final double lowerBound, final double upperBound, final double tickUnit) {
        super(localDateTimeNumberConverter, lowerBound, upperBound, tickUnit);
        tickUnitFormat.setValue(TickUnitDefault.HOURS_1.format);
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     * @param axisLabel The name to display for this axis
     */
    public LocalDateTimeAxis(final double lowerBound,
                             final double upperBound,
                             final double tickUnit,
                             final String axisLabel) {
        super(localDateTimeNumberConverter, lowerBound, upperBound, tickUnit, axisLabel);
        tickUnitFormat.setValue(TickUnitDefault.HOURS_1.format);
    }

    /**
     * Called to set the current axis range to the given range.
     * If isAnimating() is true then this method should
     * animate the range to the new range.
     *
     * @param rangeObject A range object returned from autoRange()
     * @param animate If true animate the change in range
     */
    @Override
    protected void setRange(final Object rangeObject, final boolean animate) {
        super.setRange(rangeObject, animate);
        if (!(rangeObject instanceof LocalDateTimeRange)) {
            throw new IllegalArgumentException("Range object must be of type LocalDateTimeRange!");
        }
        final LocalDateTimeRange range = (LocalDateTimeRange) rangeObject;
        tickUnitFormat.setValue(range.format);
    }

    /**
     * Get the current axis range
     * @return Range object instance
     */
    @Override
    protected Object getRange() {
        return new LocalDateTimeRange(
                getLowerBound(),
                getUpperBound(),
                getTickUnit(),
                getScale(),
                tickUnitFormat.getValue());
    }

    /**
     * Called to calculate the best range for the min and max data values
     *
     * @param minDataValue The min data value that needs to be plotted on this axis
     * @param maxDataValue The max data value that needs to be plotted on this axis
     * @param length The length of the axis in display coordinates
     * @param labelSize The approximate average size a label takes along the axis
     * @return The calculated range
     */
    @Override protected Object autoRange(final double minDataValue,
                                         final double maxDataValue,
                                         final double length,
                                         final double labelSize) {
        System.out.println("Computing auto range...");
        final Range range = (Range) super.autoRange(minDataValue, maxDataValue, length, labelSize);

        // search for the best tick unit that fits
        final TickUnitDefault tickUnitDefault = Arrays.stream(TickUnitDefault.values())
                .min(Comparator.comparingDouble(def -> Math.abs(def.tickUnit - range.tickUnit)))
                .orElse(TickUnitDefault.HOURS_1);

        final double newTickUnit = tickUnitDefault.tickUnit;
        final long lowerBound = Math.round(Math.floor(range.lowerBound / newTickUnit) * newTickUnit);
        final long upperBound = Math.round(Math.ceil(range.upperBound / newTickUnit) * newTickUnit);

        // calculate new scale
        final double newScale = calculateNewScale(length, lowerBound, upperBound);

        System.out.printf("Auto range was computed: lb:%d ub:%d tu:%.1f\n", lowerBound, upperBound, newTickUnit);
        // return new range
        return new LocalDateTimeRange(lowerBound, upperBound, newTickUnit, newScale, tickUnitDefault.format);
    }

    @Override
    protected DefaultLocalDateTimeStringConverter getDefaultStringConverter() {
        return new DefaultLocalDateTimeStringConverter(this);
    }

    /**
     * Default number formatter for LocalDateTimeAxis.
     * Stays in sync with auto-ranging and formats values appropriately.
     */
    private static class DefaultLocalDateTimeStringConverter extends StringConverter<LocalDateTime> {
        private DateTimeFormatter formatter;

        /**
         * Construct a DefaultLocalDateTimeStringConverter for the given LocalDateTimeAxis
         *
         * @param axis The axis to format tick marks for
         */
        DefaultLocalDateTimeStringConverter(final LocalDateTimeAxis axis) {
            if (Objects.isNull(axis.tickUnitFormat)) {
                formatter = DateTimeFormatter.ofPattern(TickUnitDefault.HOURS_1.format);
                System.out.println("weird stuff");
            } else {
                formatter = DateTimeFormatter.ofPattern(axis.tickUnitFormat.getValue());
                final ChangeListener<String> axisListener =
                        (observable, oldValue, newValue) -> formatter = DateTimeFormatter.ofPattern(newValue);

                axis.tickUnitFormat.addListener(axisListener);
            }
        }

        /**
         * Converts the object provided into its string form.
         * Format of the returned string is defined by this converter.
         * @return a string representation of the object passed in.
         * @see StringConverter#toString
         */
        @Override
        public String toString(final LocalDateTime localDateTime) {
            return formatter.format(localDateTime);
        }

        /**
         * Converts the string provided into a number defined by the this converter.
         * Format of the string and type of the resulting object is defined by this converter.
         * @return a Number representation of the string passed in.
         * @see StringConverter#toString
         */
        @Override
        public LocalDateTime fromString(final String string) {
            return LocalDateTime.parse(string, formatter);
        }
    }
}
