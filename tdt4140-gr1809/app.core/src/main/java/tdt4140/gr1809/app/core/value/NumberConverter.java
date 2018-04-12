package tdt4140.gr1809.app.core.value;

public interface NumberConverter<T> {
    T ofIntValue(final int intValue);

    T ofLongValue(final int longValue);

    T ofFloatValue(final int floatValue);

    T ofDoubleValue(final int doubleValue);

    int toIntValue(final T value);

    long toLongValue(final T value);

    float toFloatValue(final T value);

    double toDoubleValue(final T value);
}
