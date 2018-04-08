package tdt4140.gr1809.app.client;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;

public class NotificationClient extends BasicClient {
	
	public NotificationClient() {
		super();
	}
	
	public List<Notification> getNotificationByUserId(UUID userId) {
		final Response response = target
                .path("/user/")
                .path(userId.toString())
                .path("/notifications")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final List<Notification> notifications =
                    response.readEntity(new GenericType<List<Notification>>() {});
            response.close();
            return notifications;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get notifications of user "
                .concat(userId.toString()));
	}
	
	public void createNotification(Notification notification) {
		final Response response = target
                .path("/notifications")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(notification));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            System.out.println("Response: " + response.getStatus());
            response.close();
            throw new ClientException("Failed to create notification.");
        }
        response.close();
	}
	
	public void deleteNotification(final UUID notificationId) {
        final Response response = target
                .path("/notifications/")
                .path(notificationId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
            response.close();
            throw new ClientException("Failed to delete notification "
                    .concat(notificationId.toString()));
        }
        response.close();
    }

}
