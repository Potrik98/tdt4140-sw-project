package tdt4140.gr1809.app.datagen.generator;

import com.google.common.collect.Streams;
import org.apache.commons.math3.distribution.NormalDistribution;
import tdt4140.gr1809.app.core.model.DataPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {
    private static final Properties generatorProperties = new Properties();

    private final UUID userId;
    private final DataPoint.DataType dataType;
    private final NormalDistribution normalDistribution;

    private Generator(final UUID userId,
                        final DataPoint.DataType dataType,
                        final NormalDistribution normalDistribution) {
        this.userId = userId;
        this.dataType = dataType;
        this.normalDistribution = normalDistribution;
    }

    public static Generator getDataGenerator(final UUID userId,
                                             final DataPoint.DataType dataType) {
        if (generatorProperties.isEmpty()) {
            try {
                generatorProperties.load(Generator.class
                        .getResourceAsStream("generator.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final double mean = Double.valueOf(generatorProperties
                .getProperty(dataType.name().toLowerCase().concat("_mean")));
        final double sd = Double.valueOf(generatorProperties
                .getProperty(dataType.name().toLowerCase().concat("_sd")));
        return new Generator(userId, dataType, new NormalDistribution(mean, sd));
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

    private List<Integer> generateRandomValues(final int count) {
        return Arrays.stream(normalDistribution.sample(count))
                .mapToObj(d -> (int) Math.round(d))
                .collect(Collectors.toList());
    }
}
