package tdt4140.gr1809.app.datagen.generator;

import tdt4140.gr1809.app.core.model.DataPoint;

import java.util.List;
import java.util.UUID;

public abstract class Generator<T extends DataPoint.DataType> {
    protected final UUID userId;

    public Generator(final UUID userId) {
        this.userId = userId;
    }

    public abstract List<T> generateDataPoints(final int count);
}
