package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(builder=Statistic.StatisticBuilder.class)
public class Statistic {
    private final UUID statisticId;
    private final double value;
    private final DataPoint.DataType dataType;

    public static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    private Statistic(final UUID statisticId,
                      final double value,
                      final DataPoint.DataType dataType) {
        this.statisticId = statisticId;
        this.value = value;
        this.dataType = dataType;
    }

    public static class StatisticBuilder {
        private UUID statisticId;
        private double value;
        private DataPoint.DataType dataType;

        private StatisticBuilder(final Statistic statistic) {
            statisticId = statistic.statisticId;
            value = statistic.value;
            dataType = statistic.dataType;
        }

        private StatisticBuilder() {
            statisticId = UUID.randomUUID();
        }

        @JsonProperty("id")
        public StatisticBuilder id(final UUID id) {
            this.statisticId = id;
            return this;
        }

        @JsonProperty("value")
        public StatisticBuilder value(final double value) {
            this.value = value;
            return this;
        }

        @JsonProperty("dataType")
        public StatisticBuilder dataType(final DataPoint.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public Statistic build() {
            return new Statistic(
                    statisticId,
                    value,
                    dataType);
        }
    }

    public static StatisticBuilder builder() {
        return new StatisticBuilder();
    }

    public static StatisticBuilder from(final Statistic Statistic) {
        return new StatisticBuilder(Statistic);
    }

    public UUID getId() {
        return statisticId;
    }

    public double getValue() {
        return value;
    }

    public DataPoint.DataType getDataType() {
        return dataType;
    }
}