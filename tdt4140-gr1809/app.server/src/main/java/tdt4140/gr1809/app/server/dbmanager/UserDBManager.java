package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


public class UserDBManager extends DBManager {
    public UserDBManager() throws Exception {
        super();
    }

    public Optional<User> getUserById(final UUID userId) throws SQLException {
    	String query = "select firstName, lastName, gender, birthDate, maxPulse from Users" +
				" where userId = :userId: and deleted = 0;";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		if(!result.first()) {
			return Optional.empty();
		}
		final User user = User.builder()
				.id(userId)
                .firstName(result.getString("firstName"))
                .lastName(result.getString("lastName"))
                .gender(result.getString("gender"))
                .birthDate(Objects.isNull(result.getTimestamp("birthDate")) ? null
						: result.getTimestamp("birthDate").toLocalDateTime())
				.maxPulse(result.getInt("maxPulse"))
                .build();
        return Optional.of(user);
    }

    public void createUser(final User user) throws SQLException {
    	String query = "insert into Users (userId, firstName, lastName, gender, birthDate, maxPulse)" +
				" values (:userId:, :firstName:, :lastName:, :gender:, :birthDate:, :maxPulse:);";
    	NamedParameterStatement statement = new NamedParameterStatement(query, conn);
    	statement.setString("userId", user.getId().toString());
    	statement.setString("firstName", user.getFirstName());
    	statement.setString("lastName", user.getLastName());
    	statement.setString("gender", user.getGender());
    	statement.setTimestamp("birthDate", user.getBirthDate());
    	statement.setInt("maxPulse", user.getMaxPulse());
		statement.getStatement().executeUpdate();
    }

	public void updateUser(final User user) throws SQLException {
		String query = "update Users set" +
				" firstName = :firstName:," +
				" lastName = :lastName:," +
				" gender = :gender:," +
				" birthDate = :birthDate:," +
				" maxPulse = :maxPulse:" +
				" where userId = :userId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", user.getId().toString());
		statement.setString("firstName", user.getFirstName());
		statement.setString("lastName", user.getLastName());
		statement.setString("gender", user.getGender());
		statement.setTimestamp("birthDate", user.getBirthDate());
		statement.setInt("maxPulse", user.getMaxPulse());
		statement.getStatement().executeUpdate();
	}

    // Soft delete user
    public boolean deleteUser(final UUID userId) throws SQLException {
    	String query = "update Users set deleted = 1 where userId = :userId:;";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		return statement.getStatement().executeUpdate() == 1;
    }
}
