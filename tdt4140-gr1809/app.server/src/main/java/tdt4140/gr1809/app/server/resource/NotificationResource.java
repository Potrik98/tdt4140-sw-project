package tdt4140.gr1809.app.server.resource;

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.jetty.http.HttpStatus;

import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.NotificationDBManager;

public class NotificationResource {
	public static NotificationDBManager dbManager;
	
	public static void init() throws Exception {
		dbManager = new NotificationDBManager();
	}
	
	public static void closeConnection() throws Exception {
		dbManager.closeConnection();
	}
	
	public static String createNotification(Request req, Response res) throws SQLException {
		System.out.println("Create Notification");
		try {
			final Notification notification = Notification.mapper.readValue(req.body(), Notification.class);
			dbManager.createNotification(notification);
			res.status(HttpStatus.CREATED_201);
		} catch (IOException | NullPointerException e) {
			e.printStackTrace();
			res.status(HttpStatus.BAD_REQUEST_400);
		}
		return "";
	}
}