package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder=TimeFilter.TimeFilterBuilder.class)
public class TimeFilter {
    private final UUID id;
    private final UUID userId;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final DataPoint.DataType dataType;

    private TimeFilter(UUID id,
                       UUID userID,
                       LocalDateTime startTime,
                       LocalDateTime endTime,
                       DataPoint.DataType dataType) {
        this.id = id;
        this.userId = userID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataType = dataType;
    }

    public static class TimeFilterBuilder {
        private UUID id;
        private UUID userId;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private DataPoint.DataType dataType;

        private TimeFilterBuilder(final TimeFilter timeFilter) {
            id = timeFilter.id;
            userId = timeFilter.userId;
            startTime = timeFilter.startTime;
            endTime = timeFilter.endTime;
            dataType = timeFilter.dataType;
        }

        private TimeFilterBuilder() {
            id = UUID.randomUUID();
        }

        @JsonProperty("id")
        public TimeFilter.TimeFilterBuilder id(final UUID id) {
            this.id = id;
            return this;
        }

        @JsonProperty("userId")
        public TimeFilter.TimeFilterBuilder userId(final UUID userId) {
            this.userId = userId;
            return this;
        }

        @JsonProperty("startTime")
        public TimeFilter.TimeFilterBuilder startTime(final LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @JsonProperty("endTime")
        public TimeFilter.TimeFilterBuilder endTime(final LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        @JsonProperty("dataType")
        public TimeFilter.TimeFilterBuilder dataType(final DataPoint.DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public TimeFilter build() {
            return new TimeFilter(
                    id,
                    userId,
                    startTime,
                    endTime,
                    dataType);
        }
    }

    public static TimeFilter.TimeFilterBuilder builder() {
        return new TimeFilter.TimeFilterBuilder();
    }

    public static TimeFilter.TimeFilterBuilder from(final TimeFilter timeFilter) {
        return new TimeFilter.TimeFilterBuilder(timeFilter);
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public DataPoint.DataType getDataType() {
        return dataType;
    }
}
