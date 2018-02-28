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
	
	
	
}
