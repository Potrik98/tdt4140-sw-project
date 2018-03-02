package tdt4140.gr1809.app.server.dbmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class DataDBManager extends DBManager {

	protected DataDBManager() throws SQLException {
		super();
	}
	
	public Optional<Data> getDataById(final UUID userId, DataPoint) throws SQLException {
    	Statement stmt = null;    	
    	String query = "select Steps, Pulse, Temperature" +
    					"FROM " + this.dbName + ".Data" + 
    					"WHERE PersonID = " + userId.toString();
		stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		final Data data = new Data(
                result.getString("FirstName"),
                result.getString("LastName"),
                result.getString("Service"),
                LocalDateTime.now());
        return Optional.of(sp);
    }

}
