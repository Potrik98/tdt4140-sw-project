package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class UserResource {
    private static UserDBManager dbManager;
    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    public static void init() throws Exception {
        dbManager = new UserDBManager();
    }

    public static void init(Connection connection) {
        dbManager = new UserDBManager(connection);
    }

    public static String getUserById(Request req, Response res) throws Exception {
        System.out.println("recieved request");
        log.error("recieved request");
        UUID userId = UUID.fromString(req.params("id"));
        System.out.println("Get userId: " + userId.toString());
        Optional<User> user = dbManager.getUserById(userId);

        if (user.isPresent()) {
            res.type("application/json");
            res.status(HttpStatus.OK_200);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }

        return User.mapper.writeValueAsString(user);
    }

    public static String createUser(Request req, Response res) throws Exception {
        try {
            final User user = User.mapper.readValue(req.body(), User.class);
            System.out.print("Create: Recieved user with id : " + user.getId());
            dbManager.createUser(user);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }

    public static String updateUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("id"));
        System.out.print("Update: recieved user with id: " + userId);
        final User user = User.from(User.mapper.readValue(req.body(), User.class))
                .id(userId)
                .build();
        dbManager.updateUser(user);
        res.status(HttpStatus.OK_200);
        return "";
    }

    public static String deleteUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("id"));
        dbManager.deleteUser(userId);
        res.status(HttpStatus.NO_CONTENT_204);
        return "";
    }
}
