package tdt4140.gr1809.app.server.dbmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tdt4140.gr1809.app.core.model.ServiceProvider;


public class AccessTableDBManager extends DBManager {
	public AccessTableDBManager() throws Exception {
		super();
	}
	
	public AccessTableDBManager(Connection connection) {
		super(connection);
	}
	
	public List<ServiceProvider> getRelatedServiceProviders(UUID userId) throws SQLException{
		String query = "SELECT firstName, lastName, gender, birthDate from ServiceProviders " +
				"INNER JOIN ServiceProviderAccessToUser ON ServiceProviders.ServiceProviderId = ServiceProviderAccessToUser.ServiceProviderId " +
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
					.birthDate(result.getTimestamp("dataTime").toLocalDateTime())
					.gender(result.getString("gender"))
					.build();
			serviceProviders.add(sp);
		}
		return serviceProviders;
	}
	
	public void createRelation(UUID userId, UUID serviceProviderId) throws SQLException {
		String query = "INSERT INTO ServiceProviderAccessToUser (userId, serviceProviderId)" +
				" values (:userId:, :serviceProviderId);";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", userId.toString());
    	statement.setString("ServiceProviderId", serviceProviderId.toString());
		statement.getStatement().executeUpdate();
	}
	
}
	
