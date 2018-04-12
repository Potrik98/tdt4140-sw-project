package tdt4140.gr1809.app.core.value;

public class Numerable<T> extends Number {
    private final T value;
    private final NumberConverter<T> numberConverter;

    public class NumerableBuilder {
        private NumberConverter<T> numberConverter;

        public NumerableBuilder(final NumberConverter<T> numberConverter) {
            this.numberConverter = numberConverter;
        }

        public Numerable of(final T value) {
            return new Numerable<>(value, numberConverter);
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
