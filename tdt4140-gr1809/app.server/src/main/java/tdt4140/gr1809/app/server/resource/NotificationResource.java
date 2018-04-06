package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.Notification;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.notificationDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class NotificationResource {
	public static String createNotification(Request req, Response res) throws SQLException {
		System.out.println("Create Notification");
		try {
			final Notification notification = Notification.mapper.readValue(req.body(), Notification.class);
			notificationDBManager.createNotification(notification);
			res.status(HttpStatus.CREATED_201);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			res.status(HttpStatus.BAD_REQUEST_400);
		}
		return "";
	}
	
	public static String deleteNotification(Request req, Response res) throws Exception {
        UUID notificationId = UUID.fromString(req.params("notificationId"));
        System.out.println("Delete notificationId: " + notificationId);
        if (notificationDBManager.deleteNotification(notificationId)) {
            res.status(HttpStatus.NO_CONTENT_204);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }
        return "";
    }
}