package tdt4140.gr1809.app.server.dbmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import tdt4140.gr1809.app.core.model.DataPoint;

public class DataDBManager extends DBManager {

	protected DataDBManager() throws SQLException {
		super();
	}
	
	public Optional<DataPoint> getDataById(final UUID userId, DataPoint.DataType type) throws SQLException {
    	Statement stmt = null;    	
    	String query = "select Value, Type, Temperature, Time, Id" +
    					"FROM " + this.dbName + ".Data" + 
    					"WHERE PersonID = " + userId.toString();
		stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(query);
		final DataPoint data =  DataPoint.builder()
                .id(UUID.fromString(result.getString("ID")))
                .time(result.getTimestamp("Time").toLocalDateTime())
                .value(result.getInt("Value"))
                .dataType(DataPoint.DataType.valueOf(result.getString("Type")))
                .build();
        return Optional.of(data);
    }

}
