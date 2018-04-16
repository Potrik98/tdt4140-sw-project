package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.*;

public class UserResource {
    public static String getUserById(Request req, Response res) throws Exception {
        System.out.println("recieved request");
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("Get userId: " + userId.toString());
        Optional<User> user = userDBManager.getUserById(userId);

        if (user.isPresent()) {
            res.type("application/json");
            res.status(HttpStatus.OK_200);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }

        return User.mapper.writeValueAsString(user);
    }
    
    public static String getAllUserDataById(Request req, Response res) throws Exception {
    	final UUID userId = UUID.fromString(req.params("userID"));
    	System.out.println("Get all user data for userId: " + userId.toString());
    	final Optional<User> user = userDBManager.getUserById(userId);
    	
    	if (user.isPresent()) {
            res.type("application/json");
            res.status(HttpStatus.OK_200);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
            return "";
        }
    	
    	final List<Notification> notifications = notificationDBManager.getNotificationsByUserId(userId);
    	final List<DataPoint> datapoints = dataDBManager.getDataByUserId(userId);
    	final User userWithData = User.from(user.get())
    			.notifications(notifications)
    			.dataPoints(datapoints)
    			.build();
    	System.out.println(User.mapper.writeValueAsString(userWithData));
    	return User.mapper.writeValueAsString(userWithData);
    }

    public static String createUser(Request req, Response res) throws Exception {
        try {
            final User user = User.mapper.readValue(req.body(), User.class);
            System.out.println("Create: Recieved user with id : " + user.getId());
            userDBManager.createUser(user);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }

    public static String updateUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("Update: recieved user with id: " + userId);
        final User user = User.from(User.mapper.readValue(req.body(), User.class))
                .id(userId)
                .build();
        userDBManager.updateUser(user);
        res.status(HttpStatus.OK_200);
        return "";
    }

    public static String deleteUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("Delete userId: " + userId);
        if (userDBManager.deleteUser(userId)) {
            res.status(HttpStatus.NO_CONTENT_204);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }
        return "";
    }

    public static String getDataPointsOfUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("Datapoints of userId: " + userId);
        res.status(HttpStatus.OK_200);
        res.type("application/json");
        return User.mapper.writeValueAsString(dataDBManager.getDataByUserId(userId));
    }
    
    public static String getNotificationsOfUser(Request req, Response res) throws Exception {
    	UUID userId = UUID.fromString(req.params("userId"));
    	System.out.println("Notifications of userId: " + userId);
    	res.status(HttpStatus.OK_200);
    	res.type("application/json");
    	return User.mapper.writeValueAsString(notificationDBManager.getNotificationsByUserId(userId));
    }

    public static String getTimeFiltersOfUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("TimeFilters of userId: " + userId);
        res.status(HttpStatus.OK_200);
        res.type("application/json");
        return User.mapper.writeValueAsString(timeFilterDBManager.getTimeFiltersByUserId(userId));
    }
    
}
