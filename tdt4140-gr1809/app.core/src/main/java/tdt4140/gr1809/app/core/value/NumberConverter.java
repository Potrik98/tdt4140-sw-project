package tdt4140.gr1809.app.core.value;

public interface NumberConverter<T> {
    T ofIntValue(final int intValue);

    T ofLongValue(final long longValue);

    T ofFloatValue(final float floatValue);

    T ofDoubleValue(final double doubleValue);

    int toIntValue(final T value);

    long toLongValue(final T value);

    float toFloatValue(final T value);

    double toDoubleValue(final T value);
}
