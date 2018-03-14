package tdt4140.gr1809.app.core.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


@JsonDeserialize(builder=ServiceProvider.ServiceProviderBuilder.class)
public class ServiceProvider {
	
	private final UUID id;
	private final String firstName;
	private final String lastName;
	private final String gender;
	private final LocalDateTime birthDate;
	
	public static final ObjectMapper mapper = new ObjectMapper()
			.findAndRegisterModules();
	
	public ServiceProvider(UUID id, 
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


	public static class ServiceProviderBuilder {
		private UUID serviceProviderId;
		private String firstName;
		private String lastName;
		private String gender;
		private LocalDateTime birthDate;
		
		private ServiceProviderBuilder(final ServiceProvider serviceProvider) {
			serviceProviderId = serviceProvider.id;
			firstName = serviceProvider.firstName;
			lastName = serviceProvider.lastName;
			gender = serviceProvider.gender;
			birthDate = serviceProvider.birthDate;
		}
		
		private ServiceProviderBuilder() {
			serviceProviderId = UUID.randomUUID();
		}
		
		@JsonProperty("id")
		public ServiceProviderBuilder id(final UUID userId) {
			this.serviceProviderId = userId;
			return this;
		}

		@JsonProperty("firstName")
		public ServiceProviderBuilder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}

		@JsonProperty("lastName")
		public ServiceProviderBuilder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}

		@JsonProperty("gender")
		public ServiceProviderBuilder gender(final String gender) {
			this.gender = gender;
			return this;
		}

		@JsonProperty("birthDate")
		public ServiceProviderBuilder birthDate(final LocalDateTime birthDate) {
			this.birthDate = birthDate;
			return this;
		}
		
		public ServiceProvider build() {
			return new ServiceProvider(serviceProviderId,
					firstName,
					lastName,
					gender,
					birthDate);
		}
	}
	
	public static ServiceProviderBuilder Builder() {
		return new ServiceProviderBuilder();
	}
	
	public static ServiceProviderBuilder Builder(final ServiceProvider serviceProvider) {
		return new ServiceProviderBuilder(serviceProvider);
	}

}
