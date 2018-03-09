package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.DataPoint;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataDBManager extends DBManager {

	public DataDBManager() throws Exception {
		super();
	}

	public DataDBManager(Connection connection) {
	    super(connection);
    }
	
	public List<DataPoint> getDataByUserId(final UUID userId) throws SQLException {
    	String query = "select dataId, dataValue, dataType, dataTime from DataPoints" +
				" where userId = :userId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		List<DataPoint> dataPoints = new ArrayList<>();
		while(result.next()) {
			final DataPoint data =  DataPoint.builder()
					.id(UUID.fromString(result.getString("dataId")))
                    .userId(userId)
					.time(result.getTimestamp("dataTime").toLocalDateTime())
					.value(result.getInt("dataValue"))
					.dataType(DataPoint.DataType.valueOf(result.getString("dataType")))
					.build();
			dataPoints.add(data);
		}
        return dataPoints;
    }

	public void createDataPoint(final DataPoint dataPoint) throws SQLException {
		String query = "insert into DataPoints (dataId, userId, dataValue, dataType, dataTime)" +
				" values (:dataId:, :userId:, :dataValue:, :dataType:, :dataTime:);";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("dataId", dataPoint.getId().toString());
		statement.setString("userId", dataPoint.getUserId().toString());
		statement.setInt("dataValue", dataPoint.getValue());
		statement.setString("dataType", dataPoint.getDataType().name());
		statement.setTimestamp("dataTime", dataPoint.getTime());
		statement.getStatement().executeUpdate();
	}
}
