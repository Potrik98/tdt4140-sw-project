package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomNotificationThresholdDBManager extends DBManager {
    public CustomNotificationThresholdDBManager() throws Exception {
        super();
    }

    public List<CustomNotificationThreshold> getCustomNotificationThresholdsByUserId(final UUID userId)
            throws SQLException {
        String query = "select thresholdId, userId, dataType, thresholdType, thresholdValue" +
                " from CustomNotificationThresholds" +
                " where userId = :userId:";
        NamedParameterStatement statement = new NamedParameterStatement(query, conn);
        statement.setString("userId", userId.toString());
        ResultSet result = statement.getStatement().executeQuery();
        List<CustomNotificationThreshold> customNotificationThresholds = new ArrayList<>();
        while(result.next()) {
            final CustomNotificationThreshold customNotificationThreshold =  CustomNotificationThreshold.builder()
                    .id(UUID.fromString(result.getString("thresholdId")))
                    .userId(userId)
                    .thresholdType(CustomNotificationThreshold.ThresholdType
                            .valueOf(result.getString("thresholdType")))
                    .dataType(DataPoint.DataType.valueOf(result.getString("dataType")))
                    .value(result.getInt("thresholdValue"))
                    .build();
            customNotificationThresholds.add(customNotificationThreshold);
        }
        return customNotificationThresholds;
    }

    public void createCustomNotificationThreshold(final CustomNotificationThreshold customNotificationThreshold) throws SQLException {
        String query = "insert into CustomNotificationThresholds" +
                " (thresholdId, userId, thresholdType, dataType, thresholdValue)" +
                " values" +
                " (:thresholdId:, :userId:, :thresholdType:, :dataType:, :thresholdValue:);";
        NamedParameterStatement statement = new NamedParameterStatement(query, conn);
        statement.setString("thresholdId", customNotificationThreshold.getId().toString());
        statement.setString("userId", customNotificationThreshold.getUserId().toString());
        statement.setString("dataType", customNotificationThreshold.getDataType().name());
        statement.setString("thresholdType", customNotificationThreshold.getThresholdType().name());
        statement.setInt("thresholdValue", customNotificationThreshold.getValue());
        statement.getStatement().executeUpdate();
    }

    public void updateCustomNotificationThreshold(final CustomNotificationThreshold customNotificationThreshold)
            throws SQLException {
        String query = "update CustomNotificationThresholds set" +
                " dataType = :dataType:," +
                " thresholdType = :thresholdType:," +
                " thresholdValue = :thresholdValue:" +
                " where thresholdId = :thresholdId:";
        NamedParameterStatement statement = new NamedParameterStatement(query, conn);
        statement.setString("thresholdId", customNotificationThreshold.getId().toString());
        statement.setString("dataType", customNotificationThreshold.getDataType().name());
        statement.setString("thresholdType", customNotificationThreshold.getThresholdType().name());
        statement.setInt("thresholdValue", customNotificationThreshold.getValue());
        statement.getStatement().executeUpdate();
    }

    public void deleteCustomNotificationThreshold(final UUID thresholdId) throws SQLException {
        String query = "DELETE FROM CustomNotificationThresholds " +
                "WHERE thresholdId = :thresholdId:;";
        NamedParameterStatement statement = new NamedParameterStatement(query, conn);
        statement.setString("thresholdId", thresholdId.toString());
        statement.getStatement().executeUpdate();
    }
}
