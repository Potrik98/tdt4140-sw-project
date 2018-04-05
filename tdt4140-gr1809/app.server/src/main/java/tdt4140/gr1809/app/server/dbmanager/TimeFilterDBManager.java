package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimeFilterDBManager extends DBManager {

	public TimeFilterDBManager() throws Exception {
		super();
	}
	
	public List<TimeFilter> getTimeFiltersByUserId(final UUID userId) throws SQLException {
    	String query = "select filterId, userId, startTime, endTime, dataType from TimeFilters" +
				" where userId = :userId:";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		List<TimeFilter> timeFilters = new ArrayList<>();
		while(result.next()) {
			final TimeFilter timeFilter =  TimeFilter.builder()
					.id(UUID.fromString(result.getString("filterId")))
                    .userId(userId)
					.startTime(result.getTimestamp("startTime").toLocalDateTime())
					.endTime(result.getTimestamp("endTime").toLocalDateTime())
					.dataType(DataPoint.DataType.valueOf(result.getString("dataType")))
					.build();
			timeFilters.add(timeFilter);
		}
        return timeFilters;
    }

	public void deleteTimeFilter(final UUID timeFilterId) throws SQLException {
		String query = "DELETE FROM TimeFilters " +
				"WHERE filterId = :timeFilterId:;";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("timeFilterId", timeFilterId.toString());
		statement.getStatement().executeUpdate();
	}

	public void createTimeFilter(final TimeFilter timeFilter) throws SQLException {
		String query = "insert into TimeFilters (filterId, userId, startTime, endTime, dataType)" +
				" values (:timeFilterId:, :userId:, :startTime:, :endTime:, :dataType:);";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("timeFilterId", timeFilter.getId().toString());
		statement.setString("userId", timeFilter.getUserId().toString());
		statement.setTimestamp("startTime", timeFilter.getStartTime());
		statement.setTimestamp("endTime", timeFilter.getEndTime());
		statement.setString("dataType", timeFilter.getDataType().name());
		statement.getStatement().executeUpdate();
	}
}
