package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.UUID;

@JsonDeserialize(builder=CustomNotificationThreshold.CustomNotificationThresholdBuilder.class)
public class CustomNotificationThreshold {
    public enum ThresholdType {
        LESS_THAN,
        MORE_THAN;
    }

    private final UUID id;
    private final UUID userId;
    private final DataPoint.DataType dataType;
    private final Integer value;
    private final ThresholdType thresholdType;

    private CustomNotificationThreshold(final UUID id,
                                        final UUID userID,
                                        final DataPoint.DataType dataType,
                                        final Integer value,
                                        final ThresholdType thresholdType) {
        this.id = id;
        this.userId = userID;
        this.dataType = dataType;
        this.value = value;
        this.thresholdType = thresholdType;
    }

    public static class CustomNotificationThresholdBuilder {
        private UUID id;
        private UUID userId;
        private DataPoint.DataType dataType;
        private Integer value;
        private ThresholdType thresholdType;

        private CustomNotificationThresholdBuilder(final CustomNotificationThreshold customNotificationThreshold) {
            id = customNotificationThreshold.id;
            userId = customNotificationThreshold.userId;
            dataType = customNotificationThreshold.dataType;
            value = customNotificationThreshold.value;
            thresholdType = customNotificationThreshold.thresholdType;
        }

        private CustomNotificationThresholdBuilder() {
            id = UUID.randomUUID();
        }

        @JsonProperty("id")
        public CustomNotificationThresholdBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        @JsonProperty("userId")
        public CustomNotificationThresholdBuilder userId(final UUID userId) {
            this.userId = userId;
            return this;
        }

        @JsonProperty("dataType")
        public CustomNotificationThresholdBuilder dataType(final DataPoint.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        @JsonProperty("value")
        public CustomNotificationThresholdBuilder value(final Integer value) {
            this.value = value;
            return this;
        }

        @JsonProperty("thresholdType")
        public CustomNotificationThresholdBuilder thresholdType(final ThresholdType thresholdType) {
            this.thresholdType = thresholdType;
            return this;
        }

        public CustomNotificationThreshold build() {
            return new CustomNotificationThreshold(
                    id,
                    userId,
                    dataType,
                    value,
                    thresholdType);
        }
    }

    public static CustomNotificationThresholdBuilder builder() {
        return new CustomNotificationThresholdBuilder();
    }

    public static CustomNotificationThresholdBuilder from(final CustomNotificationThreshold customNotificationThreshold) {
        return new CustomNotificationThresholdBuilder(customNotificationThreshold);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public DataPoint.DataType getDataType() {
        return dataType;
    }

    public ThresholdType getThresholdType() {
        return thresholdType;
    }

    public Integer getValue() {
        return value;
    }
}
