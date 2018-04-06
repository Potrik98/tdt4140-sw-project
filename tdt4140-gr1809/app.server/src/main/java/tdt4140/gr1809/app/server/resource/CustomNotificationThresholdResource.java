package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.customNotificationThresholdDBManager;

public class CustomNotificationThresholdResource {
    public static String getCustomNotificationThresholdsOfUser(final Request request,
                                                               final Response response) throws Exception {
        final UUID userId = UUID.fromString(request.params("userId"));
        System.out.println("Get customNotificationThresholds for userId : " + userId);
        final List<CustomNotificationThreshold> customNotificationThresholdsOfUser =
                customNotificationThresholdDBManager.getCustomNotificationThresholdsByUserId(userId);
        response.status(HttpStatus.OK_200);
        response.type("application/json");
        return User.mapper.writeValueAsString(customNotificationThresholdsOfUser);
    }

    public static String createCustomNotificationThreshold(final Request req,
                                                           final Response res) throws Exception {
        System.out.println("Create customNotificationThreshold");
        try {
            final CustomNotificationThreshold customNotificationThreshold =
                    User.mapper.readValue(req.body(), CustomNotificationThreshold.class);
                customNotificationThresholdDBManager.createCustomNotificationThreshold(customNotificationThreshold);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }

    public static String deleteCustomNotificationThreshold(final Request request,
                                                           final Response response) throws Exception {
        final UUID customNotificationThresholdId = UUID.fromString(request.params("thresholdId"));
        System.out.println("Delete customNotificationThreshold id : " + customNotificationThresholdId);
        customNotificationThresholdDBManager.deleteCustomNotificationThreshold(customNotificationThresholdId);
        response.status(HttpStatus.NO_CONTENT_204);
        return "";
    }
}
