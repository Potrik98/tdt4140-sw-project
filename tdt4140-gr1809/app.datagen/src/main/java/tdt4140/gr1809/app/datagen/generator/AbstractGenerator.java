package tdt4140.gr1809.app.datagen.generator;

import com.google.common.collect.Streams;
import tdt4140.gr1809.app.core.model.DataPoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractGenerator {
    protected final UUID userId;
    protected final DataPoint.DataType dataType;
    protected final Random random;

    protected AbstractGenerator(final UUID userId,
                                final DataPoint.DataType dataType) {
        this.userId = userId;
        this.dataType = dataType;
        random = new Random();
    }

    public static AbstractGenerator getDataGenerator(final UUID userId,
                                                     final DataPoint.DataType dataType) {
        switch (dataType) {
            case STEPS:
                return new GeneratorSteps(userId);
            case HEART_RATE:
                return new GeneratorHeartRate(userId);
            case TEMPERATURE:
                return new GeneratorTemperature(userId);
        }
        throw new IllegalArgumentException("Unknown data type : " + dataType);
    }

    public List<DataPoint> generateDataPoints(final int count) {
        return Streams.zip(
                Stream.iterate(
                        LocalDateTime.now(),
                        time -> time.minus(dataType.timePeriod)
                ).limit(count),
                generateRandomValues(count).stream(),
                (time, value) -> DataPoint.builder()
                        .time(time)
                        .value(value)
                        .userId(userId)
                        .dataType(dataType)
                        .build()
        ).collect(Collectors.toList());
    }

    protected int getRandomNumberInInterval(int a, int b) {
        return a + random.nextInt(b - a);
    }

    public abstract List<Integer> generateRandomValues(final int count);
}
