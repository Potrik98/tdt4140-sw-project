package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@JsonDeserialize(builder=DataPoint.DataPointBuilder.class)
public class DataPoint {
	public static enum DataType {
		STEPS(Duration.of(1, ChronoUnit.DAYS)),
		TEMPERATURE(Duration.of(1, ChronoUnit.MINUTES)),
		HEART_RATE(Duration.of(10, ChronoUnit.SECONDS));
		public final Duration timePeriod;
		private DataType(final Duration timePeriod) {
			this.timePeriod = timePeriod;
		}
	}

	private final DataType dataType;
	private final UUID id;
	private final UUID userId;
	private final LocalDateTime time;
	private final int value;

	private DataPoint(UUID id, UUID userId, LocalDateTime time, int value, DataType dataType) {
		this.id = id;
		this.userId = userId;
		this.time = time;
		this.value = value;
		this.dataType = dataType;
	}

	public static class DataPointBuilder {
		private UUID id;
		private UUID userId;
		private LocalDateTime time;
		private int value;
		private DataType dataType;

		private DataPointBuilder(final DataPoint dataPoint) {
			id = dataPoint.id;
			userId = dataPoint.userId;
			time = dataPoint.time;
			value = dataPoint.value;
			dataType = dataPoint.dataType;
		}

		private DataPointBuilder() {
			id = UUID.randomUUID();
		}

		@JsonProperty("id")
		public DataPointBuilder id(final UUID id) {
			this.id = id;
			return this;
		}

		@JsonProperty("userId")
		public DataPointBuilder userId(final UUID userId) {
			this.userId = userId;
			return this;
		}

		@JsonProperty("time")
		public DataPointBuilder time(final LocalDateTime time) {
			this.time = time;
			return this;
		}

		@JsonProperty("value")
		public DataPointBuilder value(final int value) {
			this.value = value;
			return this;
		}

		@JsonProperty("dataType")
		public DataPointBuilder dataType(final DataType dataType) {
			this.dataType = dataType;
			return this;
		}

		public DataPoint build() {
			return new DataPoint(
					id,
					userId,
					time,
					value,
					dataType);
		}
	}

	public static DataPointBuilder builder() {
		return new DataPointBuilder();
	}

	public static DataPointBuilder from(final DataPoint dataPoint) {
		return new DataPointBuilder(dataPoint);
	}

	public int getValue() {
		return value;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public UUID getId() {
		return id;
	}

	public UUID getUserId() {
		return userId;
	}

	public DataType getDataType() {
		return dataType;
	}
}