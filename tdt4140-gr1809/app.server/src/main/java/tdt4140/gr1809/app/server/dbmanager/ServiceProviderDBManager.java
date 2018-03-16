package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.ServiceProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ServiceProviderDBManager extends DBManager {
	public ServiceProviderDBManager() throws Exception {
		super();
	}
	
	public ServiceProviderDBManager(Connection connection) {
		super(connection);
	}
	
	public Optional<ServiceProvider> getServiceProviderById(final UUID serviceProviderId) throws SQLException {
    	String query = "select firstName, lastName, gender, birthDate from ServiceProviders" +
				" where serviceProviderId = :serviceProviderId: and deleted = 0;";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("serviceProviderId", serviceProviderId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		if(!result.first()) {
			return Optional.empty();
		}
		final ServiceProvider serviceProvider = ServiceProvider.builder()
				.id(serviceProviderId)
                .firstName(result.getString("firstName"))
                .lastName(result.getString("lastName"))
                .gender(result.getString("gender"))
                .birthDate(result.getTimestamp("birthDate").toLocalDateTime())
                .build();
        return Optional.of(serviceProvider);
    }
	
	public void createServiceProvider(final ServiceProvider serviceProvider) throws SQLException {
    	String query = "insert into ServiceProviders (serviceProviderId, firstName, lastName, gender, birthDate)" +
				" values (:serviceProviderId:, :firstName:, :lastName:, :gender:, :birthDate:);";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("serviceProviderId", serviceProvider.getId().toString());
    	statement.setString("firstName", serviceProvider.getFirstName());
    	statement.setString("lastName", serviceProvider.getLastName());
    	statement.setString("gender", serviceProvider.getGender());
    	statement.setTimestamp("birthDate", serviceProvider.getBirthDate());
		statement.getStatement().executeUpdate();
    }

	public void updateServiceProvider(final ServiceProvider serviceProvider) throws SQLException {
		String query = "update ServiceProviders set" +
				" firstName = :firstName:," +
				" lastName = :lastName:," +
				" gender = :gender:," +
				" birthDate = :birthDate:" +
				" where serviceProviderId = :serviceProviderId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("serviceProviderId", serviceProvider.getId().toString());
		statement.setString("firstName", serviceProvider.getFirstName());
		statement.setString("lastName", serviceProvider.getLastName());
		statement.setString("gender", serviceProvider.getGender());
		statement.setTimestamp("birthDate", serviceProvider.getBirthDate());
		statement.getStatement().executeUpdate();
	}

	// Soft delete serviceProvider
	public boolean deleteServiceProvider(final UUID serviceProviderId) throws SQLException {
		String query = "update ServiceProviders set deleted = 1 where serviceProviderId = :serviceProviderId:;";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("serviceProviderId", serviceProviderId.toString());
		return statement.getStatement().executeUpdate() == 1;
	}
	
		

}
