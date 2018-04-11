package tdt4140.gr1809.app.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder=User.UserBuilder.class)
public class User {
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final String gender;
	private final LocalDateTime birthDate;
	private final Boolean participatingInAggregatedStatistics;
	@Null
	private final Integer maxPulse;
	private final List<Notification> notifications;
	private final List<DataPoint> dataPoints;
	

	public static final ObjectMapper mapper = new ObjectMapper()
			.findAndRegisterModules();

	public User(UUID id, 
			String firstName, 
			String lastName, 
			String gender, 
			LocalDateTime birthDate,
			Integer maxPulse,
			Boolean participatingInAggregatedStatistics,
			List<Notification> notifications,
			List<DataPoint> dataPoints) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.maxPulse = maxPulse;
		this.participatingInAggregatedStatistics = participatingInAggregatedStatistics;
		this.notifications = notifications;
		this.dataPoints = dataPoints;
	}


	public UUID getId() {
		return id;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public String getGender() {
		return gender;
	}

	public Integer getMaxPulse() {
		return maxPulse;
	}

	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public Boolean isParticipatingInAggregatedStatistics() {
		return participatingInAggregatedStatistics;
	}
	
	public List<Notification> getNotifications() {
		return notifications;
	}
	
	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

	public static class UserBuilder {
		private UUID userId;
		private String firstName;
		private String lastName;
		private String gender;
		private LocalDateTime birthDate;
		private Integer maxPulse;
		private Boolean participatingInAggregatedStatistics;
		private List<Notification> notifications;
		private List<DataPoint> dataPoints;

		private UserBuilder(final User user) {
			userId = user.id;
			firstName = user.firstName;
			lastName = user.lastName;
			gender = user.gender;
			birthDate = user.birthDate;
			maxPulse = user.maxPulse;
			participatingInAggregatedStatistics = user.participatingInAggregatedStatistics;
			notifications = user.notifications;
			dataPoints = user.dataPoints;
		}

		private UserBuilder() {
			userId = UUID.randomUUID();
			participatingInAggregatedStatistics = true;
		}

		@JsonProperty("id")
		public UserBuilder id(final UUID userId) {
			this.userId = userId;
			return this;
		}

		@JsonProperty("firstName")
		public UserBuilder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}

		@JsonProperty("lastName")
		public UserBuilder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}

		@JsonProperty("gender")
		public UserBuilder gender(final String gender) {
			this.gender = gender;
			return this;
		}

		@JsonProperty("birthDate")
		public UserBuilder birthDate(final LocalDateTime birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		@JsonProperty("maxPulse")
		public UserBuilder maxPulse(final Integer maxPulse) {
			this.maxPulse = maxPulse;
			return this;
		}

		@JsonProperty("participatingInAggregatedStatistics")
		public UserBuilder participatingInAggregatedStatistics(final Boolean participatingInAggregatedStatistics) {
			this.participatingInAggregatedStatistics = participatingInAggregatedStatistics;
			return  this;
		}
		
		@JsonProperty("notifications")
		public UserBuilder notifications(final List<Notification> notifications) {
			this.notifications = notifications;
			return this;
		}
		
		@JsonProperty("dataPoints")
		public UserBuilder dataPoints(final List<DataPoint> dataPoints) {
			this.dataPoints = dataPoints;
			return this;
		}

		public User build() {
			return new User(userId,
					firstName,
					lastName,
					gender,
					birthDate,
					maxPulse,
					participatingInAggregatedStatistics,
					notifications,
					dataPoints);
		}
	}

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	public static UserBuilder from(final User user) {
		return new UserBuilder(user);
	}
}
