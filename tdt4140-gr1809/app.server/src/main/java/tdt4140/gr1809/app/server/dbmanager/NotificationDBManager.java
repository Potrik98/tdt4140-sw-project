package tdt4140.gr1809.app.server.dbmanager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;

public class NotificationDBManager extends DBManager {

	public NotificationDBManager() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<Notification> getNotificationsByUserId(final UUID userId) throws SQLException {
    	String query = "select notificationId, userId, message, time from Notifications" +
				" where userId = :userId:" +
				" and deleted = 0";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("userId", userId.toString());
		ResultSet result = statement.getStatement().executeQuery();
		List<Notification> notifications = new ArrayList<>();
		while(result.next()) {
			final Notification notification =  Notification.builder()
					.id(UUID.fromString(result.getString("notificationId")))
                    .userId(userId)
					.message(result.getString("message"))
					.time(result.getTimestamp("time").toLocalDateTime())
					.build();
			notifications.add(notification);
		}
        return notifications;
    }
	
	public void createNotification(final Notification notification) throws SQLException {
		String query = "insert into Notifications (notificationId, userId, message, time)" +
				" values (:notificationId:, :userId:, :message:, :Time:);";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("notificationId", notification.getId().toString());
		statement.setString("userId", notification.getUserId().toString());
		statement.setString("message", notification.getMessage());
		statement.setTimestamp("Time", notification.getTime());
		statement.getStatement().executeUpdate();
	}
	
	// Soft delete user
    public boolean deleteNotification(final UUID notificationId) throws SQLException {
    	String query = "update Notifications set deleted = 1 where notificationId = :notificationId:;";
		NamedParameterStatement statement = new NamedParameterStatement(query, conn);
		statement.setString("notificationId", notificationId.toString());
		return statement.getStatement().executeUpdate() == 1;
    }
	
	

}
