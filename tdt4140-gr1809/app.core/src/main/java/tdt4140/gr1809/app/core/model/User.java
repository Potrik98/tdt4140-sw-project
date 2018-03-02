package tdt4140.gr1809.app.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
	
	
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final String gender;
	private final LocalDateTime birthDate;
	

	public User(UUID id, 
			String firstName, 
			String lastName, 
			String gender, 
			LocalDateTime birthDate) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
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


	public LocalDateTime getBirthDate() {
		return birthDate;
	}

	public static class UserBuilder {
		private UUID userId;
		private String firstName;
		private String lastName;
		private String gender;
		private LocalDateTime birthDate;

		private UserBuilder(final User user) {
			userId = user.id;
			firstName = user.firstName;
			lastName = user.lastName;
			gender = user.gender;
			birthDate = user.birthDate;
		}

		private UserBuilder() {
			userId = UUID.randomUUID();
		}

		public UserBuilder userId(final UUID userId) {
			this.userId = userId;
			return this;
		}

		public UserBuilder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserBuilder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserBuilder gender(final String gender) {
			this.gender = gender;
			return this;
		}

		public UserBuilder birthDate(final LocalDateTime birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public User build() {
			return new User(userId,
					firstName,
					lastName,
					gender,
					birthDate);
		}
	}

	public static UserBuilder builder() {
		return new UserBuilder();
	}

	public static UserBuilder from(final User user) {
		return new UserBuilder(user);
	}
}
