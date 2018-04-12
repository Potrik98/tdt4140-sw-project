package tdt4140.gr1809.app.core.value;

public class Numerable<T> extends Number {
    private final T value;
    private final NumberConverter<T> numberConverter;

    public static class NumerableBuilder<T> {
        private NumberConverter<T> numberConverter;

        public NumerableBuilder(final NumberConverter<T> numberConverter) {
            this.numberConverter = numberConverter;
        }

        public Numerable<T> numerableOfValue(final T value) {
            return new Numerable<>(value, numberConverter);
        }

        public Numerable<T> numerableOfInt(final int intValue) {
            return new Numerable<>(numberConverter.ofIntValue(intValue), numberConverter);
        }

        public Numerable<T> numerableOfLong(final long longValue) {
            return new Numerable<>(numberConverter.ofLongValue(longValue), numberConverter);
        }

        public Numerable<T> numerableOfFloat(final float floatValue) {
            return new Numerable<>(numberConverter.ofFloatValue(floatValue), numberConverter);
        }

        public Numerable<T> numerableOfDouble(final double doubleValue) {
            return new Numerable<>(numberConverter.ofDoubleValue(doubleValue), numberConverter);
        }
    }

    private Numerable(final T value, final NumberConverter<T> numberConverter) {
        this.value = value;
        this.numberConverter = numberConverter;
    }

    public T getValue() {
        return value;
    }

    @Override
    public int intValue() {
        return numberConverter.toIntValue(value);
    }

    @Override
    public long longValue() {
        return numberConverter.toLongValue(value);
    }

    @Override
    public float floatValue() {
        return numberConverter.toFloatValue(value);
    }

    @Override
    public double doubleValue() {
        return numberConverter.toDoubleValue(value);
    }
}
