package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder=DataPoint.DataPointBuilder.class)
public class DataPoint {
	public static enum DataType {
		STEPS,
		TEMPERATURE,
		HEART_RATE;
	}

	private final DataType dataType;
	private final UUID id;
	private final LocalDateTime time;
	private final int value;

	private DataPoint(UUID id, LocalDateTime time, int value, DataType dataType) {
		this.id = id;
		this.time = time;
		this.value = value;
		this.dataType = dataType;
	}

	public static class DataPointBuilder {
		private UUID id;
		private LocalDateTime time;
		private int value;
		private DataType dataType;

		private DataPointBuilder(final DataPoint dataPoint) {
			id = dataPoint.id;
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
			return new DataPoint(id,
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

	public DataType getDataType() {
		return dataType;
	}
}