package tdt4140.gr1809.app.ui.graph;

import com.google.common.collect.ImmutableList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.scene.chart.Axis;
import javafx.scene.chart.ValueAxis;
import javafx.util.StringConverter;
import tdt4140.gr1809.app.core.util.NumberUtils;
import tdt4140.gr1809.app.core.util.StreamUtils;
import tdt4140.gr1809.app.core.value.NumberConverter;
import tdt4140.gr1809.app.core.value.Numerable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class NumerableAxis<T> extends ValueAxis<Numerable<T>> {
    protected final Numerable.NumerableBuilder<T> numerableBuilder;

    protected static class Range {
        protected final double lowerBound;
        protected final double upperBound;
        protected final double tickUnit;
        protected final double scale;
        Range(final double lowerBound,
              final double upperBound,
              final double tickUnit,
              final double scale) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            this.tickUnit = tickUnit;
            this.scale = scale;
        }
    }

    /*
     * When true zero is always included in the visible range. This only has effect if auto-ranging is on.
     */
    protected BooleanProperty forceZeroInRange = new BooleanPropertyBase(true) {
        @Override protected void invalidated() {
            // This will effect layout if we are auto ranging
            if(isAutoRanging()) requestAxisLayout();
        }

        @Override
        public Object getBean() {
            return NumerableAxis.this;
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
     * The value between each major tick mark in data units. This is automatically set if we are auto-ranging.
     */
    protected DoubleProperty tickUnit = new DoublePropertyBase(5) {
        @Override protected void invalidated() {
            if(!isAutoRanging()) {
                invalidateRange();
                requestAxisLayout();
            }
        }

        @Override
        public Object getBean() {
            return NumerableAxis.this;
        }

        @Override
        public String getName() {
            return "tickUnit";
        }
    };

    protected double getTickUnit() { return tickUnit.get(); }
    protected void setTickUnit(final double value) { tickUnit.set(value); }
    public final DoubleProperty tickUnitProperty() { return tickUnit; }

    /**
     * Creates an auto-ranging time axis
     *
     * @param numberConverter Number converter to convert from T to Numerable<T>
     */
    public NumerableAxis(final NumberConverter<T> numberConverter) {
        numerableBuilder = new Numerable.NumerableBuilder<>(numberConverter);
        forceZeroInRange.set(false);
        setTickLabelFormatter(getDefaultTickLabelFormatter());
        setAutoRanging(true);
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param numberConverter Number converter to convert from T to Numerable<T>
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     */
    public NumerableAxis(final NumberConverter<T> numberConverter,
                         final double lowerBound,
                         final double upperBound,
                         final double tickUnit) {
        super(lowerBound, upperBound);
        setTickUnit(tickUnit);
        numerableBuilder = new Numerable.NumerableBuilder<>(numberConverter);
        forceZeroInRange.set(false);
        setTickLabelFormatter(getDefaultTickLabelFormatter());
        setAutoRanging(false);
    }

    /**
     * Create a non-auto-ranging LocalDateTimeAxis with the given upper bound, lower bound and tick unit
     *
     * @param lowerBound The lower bound for this axis
     * @param upperBound The upper bound for this axis
     * @param tickUnit The tick unit
     * @param axisLabel The name to display for this axis
     */
    public NumerableAxis(final NumberConverter<T> numberConverter,
                         final double lowerBound,
                         final double upperBound,
                         final double tickUnit,
                         final String axisLabel) {
        this(numberConverter, lowerBound, upperBound, tickUnit);
        setLabel(axisLabel);
    }

    /**
     * Calculate a list of the data values for every minor tick mark
     *
     * @return List of data values where to draw minor tick marks
     */
    @Override
    protected List<Numerable<T>> calculateMinorTickMarks() {
        final double minorUnit = getTickUnit() / getMinorTickCount();
        if (minorUnit > 0 && getUpperBound() > getLowerBound()) {
            return StreamUtils.takeWhile(
                    DoubleStream.iterate(getLowerBound(), d -> d + minorUnit).boxed(),
                    d -> d < getUpperBound())
                    .map(numerableBuilder::numerableOfDouble)
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
    }

    /**
     * Get the current axis range
     * @return Range object instance
     */
    @Override
    protected Object getRange() {
        return new Range(
                getLowerBound(),
                getUpperBound(),
                getTickUnit(),
                getScale());
    }

    /**
     * Calculate a list of all the data values for each tick mark in range
     *
     * @param length The length of the axis in display units
     * @param rangeObject A range object returned from autoRange()
     * @return A list of tick marks that fit along the axis if it was the given length
     */
    @Override
    protected List<Numerable<T>> calculateTickValues(final double length, final Object rangeObject) {
        if (!(rangeObject instanceof Range)) {
            throw new IllegalArgumentException("Range object must be of type Range!");
        }
        final Range range = (Range) rangeObject;

        if (range.tickUnit > 0 && range.upperBound > range.lowerBound) {
            return StreamUtils.takeWhile(
                    DoubleStream.iterate(range.lowerBound, d -> d + range.tickUnit).boxed(),
                    d -> d < range.upperBound)
                    .map(numerableBuilder::numerableOfDouble)
                    .collect(Collectors.toList());
        } else {
            return ImmutableList.of();
        }
    }

    /**
     * Get the tick mark label of a value
     * @param value Tick mark value
     * @return Label for the value
     */
    @Override
    protected String getTickMarkLabel(final Numerable<T> value) {
        return getTickLabelFormatter().toString(value);
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
        // If zero is not in the range, and we're forcing zero, set zero to one of the end points.
        final double maxValue = isForceZeroInRange() && maxDataValue < 0 ? 0 : maxDataValue;
        final double minValue = isForceZeroInRange() && minDataValue > 0 ? 0 : minDataValue;

        final double range = maxValue - minValue;

        // calculate the number of tick-marks we can fit in the given length
        final int numberOfTickMarks = (int) NumberUtils.clamp(2, 20,
                Math.floor(Math.abs(length) / labelSize));

        // calculate tick unit for the number of ticks can have in the given data range
        final double tickUnit = range / (double) numberOfTickMarks;

        // calculate new scale
        final double newScale = calculateNewScale(length, minValue, maxValue);

        // return new range
        return new Range(minValue, maxValue, tickUnit, newScale);
    }

    /**
     * Get the default tick label formatter
     * @return TickLabelFormatter converting between labels and data values
     */
    protected StringConverter<Numerable<T>> getDefaultTickLabelFormatter() {
        final StringConverter<T> stringConverter = getDefaultStringConverter();
        return new StringConverter<Numerable<T>>() {
            @Override
            public String toString(final Numerable<T> tNumerable) {
                return stringConverter.toString(tNumerable.getValue());
            }

            @Override
            public Numerable<T> fromString(final String s) {
                return numerableBuilder.numerableOfValue(stringConverter.fromString(s));
            }
        };
    }

    /**
     * Get the default string converter for this axis
     * @return Default string converter
     */
    protected StringConverter<T> getDefaultStringConverter() {
        return new DefaultStringConverter();
    }

    /**
     * Get an unwrapped axis (without Numerable) so that the
     * axis can be used more easily.
     * @return Unwrapped axis of type Axis<T>
     */
    public Axis<T> getUnwrappedAxis() {
        return new UnwrappedAxis(this);
    }

    /**
     * Inner class un-wrapper for an unwrapped axis (without Numerable),
     * so that the axis can be used more easily.
     */
    private class UnwrappedAxis extends Axis<T> {
        final NumerableAxis<T> numerableAxis;

        private UnwrappedAxis(final NumerableAxis<T> numerableAxis) {
            this.numerableAxis = numerableAxis;
            this.setAutoRanging(numerableAxis.isAutoRanging());
            this.setLabel(numerableAxis.getLabel());
        }

        @Override
        protected Object autoRange(double v) {
            return numerableAxis.autoRange(v);
        }

        @Override
        protected void setRange(Object o, boolean b) {
            numerableAxis.setRange(o, b);
        }

        @Override
        protected Object getRange() {
            return numerableAxis.getRange();
        }

        @Override
        public double getZeroPosition() {
            return numerableAxis.getZeroPosition();
        }

        @Override
        public double getDisplayPosition(T t) {
            return numerableAxis.getDisplayPosition(numerableBuilder.numerableOfValue(t));
        }

        @Override
        public T getValueForDisplay(double v) {
            return numerableAxis.getValueForDisplay(v).getValue();
        }

        @Override
        public boolean isValueOnAxis(T t) {
            return numerableAxis.isValueOnAxis(numerableBuilder.numerableOfValue(t));
        }

        @Override
        public double toNumericValue(T t) {
            return numerableAxis.toNumericValue(numerableBuilder.numerableOfValue(t));
        }

        @Override
        public T toRealValue(double v) {
            return numerableAxis.toRealValue(v).getValue();
        }

        @Override
        protected List<T> calculateTickValues(double v, Object o) {
            return numerableAxis.calculateTickValues(v, o).stream()
                    .map(Numerable::getValue).collect(Collectors.toList());
        }

        @Override
        protected String getTickMarkLabel(T t) {
            return numerableAxis.getTickMarkLabel(numerableBuilder.numerableOfValue(t));
        }

        @Override
        public void invalidateRange(final List<T> list) {
            numerableAxis.invalidateRange(list.stream()
                    .map(numerableBuilder::numerableOfValue).collect(Collectors.toList()));
        }

        @Override
        protected void layoutChildren() {
            numerableAxis.layoutChildren();
        }

        @Override
        protected void tickMarksUpdated() {
            numerableAxis.tickMarksUpdated();
        }
    }

    /**
     * Default string converter using toString method
     */
    protected class DefaultStringConverter extends StringConverter<T> {
        private final Map<String, T> values;

        protected DefaultStringConverter() {
            values = new HashMap<>();
        }

        @Override
        public String toString(final T t) {
            final String key = t.toString();
            values.put(key, t);
            return key;
        }

        @Override
        public T fromString(final String s) {
            return values.get(s);
        }
    }
}
