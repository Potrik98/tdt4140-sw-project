package tdt4140.gr1809.app.ui.graph;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Dimension2D;
import javafx.geometry.Side;
import javafx.scene.chart.ValueAxis;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.core.util.NumberUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class LocalDateTimeAxis extends ValueAxis<Long> {
    private static class Range {
        final long lowerBound;
        final long upperBound;
        final double tickUnit;
        final double scale;
        final String format;
        Range(final long lowerBound,
              final long upperBound,
              final double tickUnit,
              final double scale,
              final String format) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.tickUnit = tickUnit;
            this.scale = scale;
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
     * When true zero is always included in the visible range. This only has effect if auto-ranging is on.
     */
    private BooleanProperty forceZeroInRange = new BooleanPropertyBase(true) {
        @Override protected void invalidated() {
            // This will effect layout if we are auto ranging
            if(isAutoRanging()) requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return LocalDateTimeAxis.this;
        }

        @Override
        public String getName() {
            return "forceZeroInRange";
        }
    };

    public final boolean isForceZeroInRange() { return forceZeroInRange.getValue(); }
    public final void setForceZeroInRange(boolean value) { forceZeroInRange.setValue(value); }
    public final BooleanProperty forceZeroInRangeProperty() { return forceZeroInRange; }

    /*
     * The format used for the tick unit marks
     */
    private StringProperty tickUnitFormat = new StringPropertyBase("dd-MMM HH:mm") {
        @Override
        public Object getBean() {
            return LocalDateTimeAxis.this;
        }

        @Override
        public String getName() {
            return "tickUnitFormat";
        }
    };

    /*
     * The value between each major tick mark in data units. This is automatically set if we are auto-ranging.
     */
    private DoubleProperty tickUnit = new DoublePropertyBase(5) {
        @Override protected void invalidated() {
            if(!isAutoRanging()) {
                invalidateRange();
                requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return LocalDateTimeAxis.this;
        }

        @Override
        public String getName() {
            return "tickUnit";
        }
    };

    private double getTickUnit() { return tickUnit.get(); }
    private void setTickUnit(final double value) { tickUnit.set(value); }
    public final DoubleProperty tickUnitProperty() { return tickUnit; }

    /**
     * Creates an auto-ranging time axis
     */
    public LocalDateTimeAxis() {
        forceZeroInRange.set(false);
        setTickLabelFormatter(getDefaultFormatter());
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     */
    public LocalDateTimeAxis(final double lowerBound, final double upperBound, final double tickUnit) {
        super(lowerBound, upperBound);
        setTickUnit(tickUnit);
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param axisLabel The name to display for this axis
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     */
    public LocalDateTimeAxis(final String axisLabel,
                             final double lowerBound,
                             final double upperBound,
                             final double tickUnit) {
        super(lowerBound, upperBound);
        setTickUnit(tickUnit);
        setLabel(axisLabel);
    }

    /**
     * Calculate a list of the data values for every minor tick mark
     *
     * @return List of data values where to draw minor tick marks
     */
    @Override
    protected List<Long> calculateMinorTickMarks() {
        final double minorUnit = getTickUnit() / getMinorTickCount();
        if (minorUnit > 0 && getUpperBound() > getLowerBound()) {
            return DoubleStream.iterate(getLowerBound(), d -> d + minorUnit)
                    .limit((long) getUpperBound())
                    .mapToObj(d -> ((long) d))
                    .collect(Collectors.toList());
        } else if (getUpperBound() < getLowerBound()) {
            return DoubleStream.iterate(getUpperBound(), d -> d - minorUnit)
                    .limit((long) getLowerBound())
                    .map(d -> -d)
                    .mapToObj(d -> ((long) d))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
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
        if (!(rangeObject instanceof Range)) {
            throw new IllegalArgumentException("Range object must be of type Range!");
        }
        final Range range = (Range) rangeObject;

        final double oldLowerBound = getLowerBound();
        setLowerBound(range.lowerBound);
        setUpperBound(range.upperBound);
        setTickUnit(range.tickUnit);
        currentLowerBound.set(range.lowerBound);
        setScale(range.scale);
        tickUnitFormat.setValue(range.format);
    }

    /**
     * Get the current axis range
     * @return Range object instance
     */
    @Override
    protected Object getRange() {
        return new Range(
                (long) getLowerBound(),
                (long) getUpperBound(),
                getTickUnit(),
                getScale(),
                tickUnitFormat.getValue());
    }

    /**
     * Calculate a list of all the data values for each tick mark in range
     *
     * @param length The length of the axis in display units
     * @param rangeObject A range object returned from autoRange()
     * @return A list of tick marks that fit along the axis if it was the given length
     */
    @Override
    protected List<Long> calculateTickValues(double length, Object rangeObject) {
        if (!(rangeObject instanceof Range)) {
            throw new IllegalArgumentException("Range object must be of type Range!");
        }
        final Range range = (Range) rangeObject;

        if (range.tickUnit > 0 && range.upperBound > range.lowerBound) {
            return DoubleStream.iterate(range.lowerBound, d -> d + range.tickUnit)
                    .limit(range.upperBound)
                    .mapToObj(d -> ((long) d))
                    .collect(Collectors.toList());
        } else if (range.upperBound < range.lowerBound) {
            return DoubleStream.iterate(range.upperBound, d -> d - range.tickUnit)
                    .limit(range.lowerBound)
                    .map(d -> -d)
                    .mapToObj(d -> ((long) d))
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    @Override
    protected String getTickMarkLabel(final Long value) {
        final StringConverter<Long> formatter = Objects.isNull(getTickLabelFormatter())
                ? getDefaultFormatter()
                : getTickLabelFormatter();
        return formatter.toString(value);
    }

    /**
     * Called to set the upper and lower bound and anything else that needs to be auto-ranged
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
        final Side side = getSide();
        final boolean vertical = Side.LEFT.equals(side) || Side.RIGHT.equals(side);

        // If zero is not in the range, and we're forcing zero, set zero to one of the end points.
        final double maxValue = isForceZeroInRange() && maxDataValue < 0 ? 0 : maxDataValue;
        final double minValue = isForceZeroInRange() && minDataValue > 0 ? 0 : minDataValue;

        final double range = maxValue - minValue;

        // calculate the number of tick-marks we can fit in the given length
        final int numberOfTickMarks = (int) NumberUtils.clamp(2, 20,
                Math.floor(Math.abs(length) / labelSize));

        // calculate tick unit for the number of ticks can have in the given data range
        final double tickUnit = range / (double) numberOfTickMarks;

        // search for the best tick unit that fits
        final TickUnitDefault tickUnitDefault = Arrays.stream(TickUnitDefault.values())
                .min(Comparator.comparingDouble(def -> Math.abs(def.tickUnit - tickUnit)))
                .orElse(TickUnitDefault.HOURS_1);

        final double newTickUnit = tickUnitDefault.tickUnit;
        final long lowerBounds = Math.round(Math.floor(minValue / newTickUnit) * newTickUnit);
        final long upperBounds = Math.round(Math.ceil(maxValue / newTickUnit) * newTickUnit);

        // calculate new scale
        final double newScale = calculateNewScale(length, lowerBounds, upperBounds);

        // return new range
        return new Range(lowerBounds, upperBounds, tickUnit, newScale, tickUnitDefault.format);
    }

    /**
     * Measure the size of the label for given tick mark value. This uses the font that is set for the tick marks
     *
     * @param value tick mark value
     * @param rangeObject range to use during calculations
     * @return size of tick mark label for given value
     */
    @Override protected Dimension2D measureTickMarkSize(final Long value, final Object rangeObject) {
        if (!(rangeObject instanceof Range)) {
            throw new IllegalArgumentException("Range object must be of type Range!");
        }
        final Range range = (Range) rangeObject;
        final StringConverter<Long> formatter = Objects.isNull(getTickLabelFormatter())
                ? getDefaultFormatter(range)
                : getTickLabelFormatter();
        final String labelText = formatter.toString(value);
        return measureTickMarkLabelSize(labelText, getTickLabelRotation());
    }

    private DefaultFormatter getDefaultFormatter(final Range range) {
        return new DefaultFormatter(this, range);
    }

    private DefaultFormatter getDefaultFormatter() {
        return new DefaultFormatter(this);
    }

    /**
     * Default number formatter for LocalDateTimeAxis.
     * Stays in sync with auto-ranging and formats values appropriately.
     */
    public static class DefaultFormatter extends StringConverter<Long> {
        private DateTimeFormatter formatter;

        /**
         * Construct a DefaultFormatter for the given LocalDateTimeAxis
         *
         * @param axis The axis to format tick marks for
         */
        DefaultFormatter(final LocalDateTimeAxis axis) {
            formatter = getFormatter(axis);

            final ChangeListener<String> axisListener =
                    (observable, oldValue, newValue) -> formatter = getFormatter(axis);

            axis.tickUnitFormat.addListener(axisListener);
        }

        /**
         * Construct a DefaultFormatter for a given range
         */
        DefaultFormatter(final LocalDateTimeAxis axis, final Range range) {
            this(axis);
            formatter = DateTimeFormatter.ofPattern(range.format);
        }

        private static DateTimeFormatter getFormatter(final LocalDateTimeAxis axis) {
            return DateTimeFormatter.ofPattern(axis.tickUnitFormat.getValue());
        }

        /**
         * Converts the object provided into its string form.
         * Format of the returned string is defined by this converter.
         * @return a string representation of the object passed in.
         * @see StringConverter#toString
         */
        @Override
        public String toString(final Long epochSeconds) {
            final LocalDateTime time = LocalDateTime.ofEpochSecond(epochSeconds, 0, ZoneOffset.UTC);
            return formatter.format(time);
        }

        /**
         * Converts the string provided into a number defined by the this converter.
         * Format of the string and type of the resulting object is defined by this converter.
         * @return a Number representation of the string passed in.
         * @see StringConverter#toString
         */
        @Override
        public Long fromString(final String string) {
            return LocalDateTime.parse(string, formatter).toEpochSecond(ZoneOffset.UTC);
        }
    }
}
