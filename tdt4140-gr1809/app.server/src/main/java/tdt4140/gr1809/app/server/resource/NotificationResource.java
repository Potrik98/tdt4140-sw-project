package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.Notification;

import java.io.IOException;
import java.sql.SQLException;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.notificationDBManager;

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
}