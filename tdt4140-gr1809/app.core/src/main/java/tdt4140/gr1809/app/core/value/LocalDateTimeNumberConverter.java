package tdt4140.gr1809.app.core.value;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeNumberConverter implements NumberConverter<LocalDateTime> {
    @Override
    public LocalDateTime ofIntValue(int intValue) {
        return ofLongValue(intValue);
    }

    @Override
    public LocalDateTime ofLongValue(int longValue) {
        return LocalDateTime.ofEpochSecond(longValue, 0, ZoneOffset.UTC);
    }

    @Override
    public LocalDateTime ofFloatValue(int floatValue) {
        return ofLongValue(Math.round(floatValue));
    }

    @Override
    public LocalDateTime ofDoubleValue(int doubleValue) {
        return ofLongValue(Math.round(doubleValue));
    }

    @Override
    public int toIntValue(final LocalDateTime value) {
        return (int) toLongValue(value);
    }

    @Override
    public long toLongValue(final LocalDateTime value) {
        return value.toEpochSecond(ZoneOffset.UTC);
    }

    @Override
    public float toFloatValue(final LocalDateTime value) {
        return (float) toLongValue(value);
    }

    @Override
    public double toDoubleValue(final LocalDateTime value) {
        return (double) toLongValue(value);
    }
}
