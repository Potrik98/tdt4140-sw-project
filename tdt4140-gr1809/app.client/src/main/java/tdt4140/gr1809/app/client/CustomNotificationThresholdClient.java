package tdt4140.gr1809.app.client;

import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

public class CustomNotificationThresholdClient extends BasicClient {
    public CustomNotificationThresholdClient() {
        super();
    }

    public List<CustomNotificationThreshold> getCustomNotificationThresholdsForUserId(final UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .path("/customnotificationthresholds")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final List<CustomNotificationThreshold> customNotificationThresholds =
                    response.readEntity(new GenericType<List<CustomNotificationThreshold>>() {});
            response.close();
            return customNotificationThresholds;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get CustomNotificationThresholds of user "
                .concat(userId.toString()));
    }

    public void createCustomNotificationThreshold(final CustomNotificationThreshold customNotificationThreshold) {
        final Response response = target
                .path("/customnotificationthresholds")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(customNotificationThreshold));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            System.out.println("Response: " + response.getStatus());
            response.close();
            throw new ClientException("Failed to create customNotificationThreshold.");
        }
        response.close();
    }

    public void deleteCustomNotificationThreshold(final UUID customNotificationThresholdId) {
        final Response response = target
                .path("/customnotificationthresholds/")
                .path(customNotificationThresholdId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
            System.out.println("Response: " + response.getStatus());
            response.close();
            throw new ClientException("Failed to delete customNotificationThreshold id :"
                    + customNotificationThresholdId);
        }
        response.close();
    }
}
