package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonDeserialize(builder=Notification.NotificationBuilder.class)
public class Notification {
	private final UUID notificationId;
	private final UUID userId;
	private final LocalDateTime time;
	private final String message;

	private Notification(UUID notificationId, UUID userId, LocalDateTime time, String message) {
		this.notificationId = notificationId;
		this.userId = userId;
		this.time = time;
		this.message = message;
	}

	public static class NotificationBuilder {
		private UUID notificationId;
		private UUID userId;
		private LocalDateTime time;
		private String message;

		private NotificationBuilder(final Notification notification) {
			notificationId = notification.notificationId;
			userId = notification.userId;
			time = notification.time;
			message = notification.message;
			
		}

		private NotificationBuilder() {
			notificationId = UUID.randomUUID();
		}

		@JsonProperty("id")
		public NotificationBuilder id(final UUID id) {
			this.notificationId = id;
			return this;
		}

		@JsonProperty("userId")
		public NotificationBuilder userId(final UUID userId) {
			this.userId = userId;
			return this;
		}

		@JsonProperty("time")
		public NotificationBuilder time(final LocalDateTime time) {
			this.time = time;
			return this;
		}

		@JsonProperty("message")
		public NotificationBuilder message(final String message) {
			this.message = message;
			return this;
		}

		public Notification build() {
			return new Notification(
					notificationId,
					userId,
					time,
					message);
		}
	}

	public static NotificationBuilder builder() {
		return new NotificationBuilder();
	}

	public static NotificationBuilder from(final Notification Notification) {
		return new NotificationBuilder(Notification);
	}

	public LocalDateTime getTime() {
		return time;
	}

	public UUID getId() {
		return notificationId;
	}

	public UUID getUserId() {
		return userId;
	}

	public String getMessage() {
		return message;
	}
}