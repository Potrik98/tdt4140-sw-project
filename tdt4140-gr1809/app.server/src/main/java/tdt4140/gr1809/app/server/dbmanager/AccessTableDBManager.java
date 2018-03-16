package tdt4140.gr1809.app.server.dbmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;


public class AccessTableDBManager extends DBManager {
	public AccessTableDBManager() throws Exception {
		super();
	}
	
	public AccessTableDBManager(Connection connection) {
		super(connection);
	}
	
	public List<ServiceProvider> getRelatedServiceProviders(UUID userId) throws SQLException {
		String query = "SELECT firstName, ServiceProviders.serviceProviderId, lastName, gender, birthDate from ServiceProviderAccessToUser " +
				"INNER JOIN ServiceProviders ON ServiceProviders.ServiceProviderId = ServiceProviderAccessToUser.ServiceProviderId " +
				"WHERE userId = :userId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		List<ServiceProvider> serviceProviders = new ArrayList<>();
		while(result.next()) {
			final ServiceProvider sp =  ServiceProvider.builder()
					.id(UUID.fromString(result.getString("serviceProviderId")))
					.firstName(result.getString("firstName"))
					.lastName(result.getString("lastName"))
					.birthDate(result.getTimestamp("birthdate").toLocalDateTime())
					.gender(result.getString("gender"))
					.build();
			serviceProviders.add(sp);
		}
		return serviceProviders;
	}
	
	public List<User> getRelatedUsers(UUID serviceProviderId) throws SQLException {
		String query = "SELECT firstName, Users.userId, lastName, gender, birthDate from ServiceProviderAccessToUser " +
				"INNER JOIN Users ON Users.UserId = ServiceProviderAccessToUser.UserId " +
				"WHERE ServiceProviderAccessToUser.serviceProviderId = :serviceProviderId: AND Deleted = 0";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("serviceProviderId", serviceProviderId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		List<User> users = new ArrayList<>();
		while(result.next()) {
			final User user =  User.builder()
					.id(UUID.fromString(result.getString("userId")))
					.firstName(result.getString("firstName"))
					.lastName(result.getString("lastName"))
					.birthDate(result.getTimestamp("birthdate").toLocalDateTime())
					.gender(result.getString("gender"))
					.build();
			users.add(user);
		}
		return users;
	}
	
	
	public void createRelation(UUID userId, UUID serviceProviderId) throws SQLException {
		String query = "INSERT INTO ServiceProviderAccessToUser (userId, serviceProviderId)" +
				" values (:userId:, :serviceProviderId:);";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", userId.toString());
    	statement.setString("serviceProviderId", serviceProviderId.toString());
		statement.getStatement().executeUpdate();
	}
	
	public void removeRelation(UUID userId, UUID serviceProviderId) throws SQLException {
		String query = "DELETE FROM ServiceProviderAccessToUser " +
				"WHERE userId = :userId: AND serviceProviderId = :serviceProviderId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		statement.setString("serviceProviderId", serviceProviderId.toString());
		statement.getStatement().executeUpdate();
	}	
}
	
