package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;


public class UserDBManager extends DBManager {
    public UserDBManager() throws SQLException {
        super();
    }

    public UserDBManager(Connection connection) {
    	super(connection);
	}

    public Optional<User> getUserById(final UUID userId) throws SQLException {
    	String query = "select count(*) as count, firstName, lastName, gender, birthDate from Users" +
				" where userId = :userId: and deleted = 0;";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		result.first();
		if (result.getInt("count") == 0) {
			return Optional.empty();
		}
		final User user = User.builder()
				.id(userId)
                .firstName(result.getString("firstName"))
                .lastName(result.getString("lastName"))
                .gender(result.getString("gender"))
                .birthDate(result.getTimestamp("birthDate").toLocalDateTime())
                .build();
        return Optional.of(user);
    }

    public void putUser(final User user) throws SQLException {
    	String query = "insert into Users (userId, firstName, lastName, gender, birthDate)" +
				" values (:userId:, :firstName:, :lastName:, :gender:, :birthDate:);" ;//+
				//" on duplicate key update firstName, lastName, gender, birthDate, deleted = 0;";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", user.getId().toString());
    	statement.setString("firstName", user.getFirstName());
    	statement.setString("lastName", user.getLastName());
    	statement.setString("gender", user.getGender());
    	statement.setTimestamp("birthDate", user.getBirthDate());
		statement.getStatement().executeUpdate();
    }

    // Soft delete user
    public boolean deleteUser(final UUID userId) throws SQLException {
    	String query = "update Users set deleted = 1 where userId = :userId:;";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		return statement.getStatement().executeUpdate() == 1;
    }
}
