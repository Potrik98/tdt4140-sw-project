package tdt4140.gr1809.app.ui.graph;

import javafx.geometry.Side;
import javafx.scene.chart.Axis;
import tdt4140.gr1809.app.core.util.StreamUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TimeAxis extends Axis<LocalDateTime> {
    private TimeRange range;

    public static class TimeRange {
        public final LocalDateTime startTime;
        public final LocalDateTime endTime;
        public final ChronoUnit unit;
        public final int stepSize;
        public TimeRange(LocalDateTime startTime, LocalDateTime endTime, int stepSize, ChronoUnit unit) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.stepSize = stepSize;
            this.unit = unit;
        }
    }

    public TimeAxis() {
        super();
        setRange(autoRange(0.0), true);
        setLabel("Time");
        setAutoRanging(false);
        setSide(Side.BOTTOM);
        setAnimated(false);
    }

    protected List<LocalDateTime> calculateMinorTickMarks() {
        final List<LocalDateTime> list = StreamUtils.takeWhile(
                Stream.iterate(range.endTime, time -> time.minus(15, ChronoUnit.MINUTES)),
                time -> time.isAfter(range.startTime))
                .collect(Collectors.toList());
        list.add(range.startTime);
        return list;
    }

    protected Object autoRange(double v) {
        return new TimeRange(LocalDateTime.now().minus(1, ChronoUnit.DAYS),
                LocalDateTime.now(), 1, ChronoUnit.HOURS);
    }

    protected void setRange(Object o, boolean b) {
        if (!(o instanceof TimeRange)) {
            throw new IllegalArgumentException("Range object must be of type TimeRange!");
        }
        range = (TimeRange) o;
    }

    protected Object getRange() {
        return range;
    }

    public double getZeroPosition() {
        return 0;
    }

    public double getDisplayPosition(LocalDateTime localDateTime) {
        return toNumericValue(localDateTime) - toNumericValue(range.startTime);
    }

    public LocalDateTime getValueForDisplay(double v) {
        return toRealValue(v + toNumericValue(range.startTime));
    }

    public boolean isValueOnAxis(LocalDateTime localDateTime) {
        return localDateTime.isAfter(range.startTime) && localDateTime.isBefore(range.endTime);
    }

    public double toNumericValue(LocalDateTime localDateTime) {
        return localDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    public LocalDateTime toRealValue(double v) {
        return LocalDateTime.ofEpochSecond(Math.round(v), 0, ZoneOffset.UTC);
    }

    protected List<LocalDateTime> calculateTickValues(double v, Object o) {
        final List<LocalDateTime> list = StreamUtils.takeWhile(
                Stream.iterate(range.endTime, time -> time.minus(range.stepSize, range.unit)),
                time -> time.isAfter(range.startTime))
                .collect(Collectors.toList());
        list.add(range.startTime);
        return list;
    }

    protected String getTickMarkLabel(LocalDateTime localDateTime) {
        return localDateTime.getMonth().toString() +
                " " +
                localDateTime.getDayOfMonth() +
                " " +
                localDateTime.getHour() +
                ":" +
                localDateTime.getMinute();
    }
}
