package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.User;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


public class UserDBManager extends DBManager {
    public UserDBManager() throws SQLException {
        super();
    }
    

    public Optional<User> getUserById(final UUID userId) throws SQLException {
    	Statement stmt = null;
    	
    	String query = "select Name, Gender, PhoneNum" +
    					"FROM " + this.dbName + ".Users" + 
    					"WHERE PersonID = " + userId.toString();
		stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		final User user = new User(
                result.getString("FirstName"),
                result.getString("LastName"),
                result.getString("gender"),
                LocalDateTime.now());
        return Optional.of(user);
    }
    

    public void putUser(final User user) throws SQLException {
    	Statement stmt = null;
    	String update = "INSERT INTO " + this.dbName + ".Users" + 
    	"VALUES ( " + user.getFirstName() + ", " + user.getLastName() + ", " +
    			 user.getGender() + ", " + user.getBirthDate() + ");";
		stmt = conn.createStatement();
		stmt.executeUpdate(update);
    }

    
    // Soft delete user
    public void deleteUser(final UUID userId) throws SQLException {
    	Statement stmt = null;
    	String update = "INSERT INTO " + this.dbName + ".Users (Deleted)" +
    	"VALUES (1);";
		stmt = conn.createStatement();
		stmt.executeUpdate(update);
    }
}
