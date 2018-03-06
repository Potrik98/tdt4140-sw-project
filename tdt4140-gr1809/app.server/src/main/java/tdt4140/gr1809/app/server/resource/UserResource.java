package tdt4140.gr1809.app.server.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
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
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    public static void init() throws SQLException {
        dbManager = new UserDBManager();
    }

    public static void init(Connection connection) {
        dbManager = new UserDBManager(connection);
    }

    public static Response getUserById(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("id"));
        Optional<User> user = dbManager.getUserById(userId);

        if (user.isPresent()) {
            res.status(HttpStatus.OK_200);
            res.body(mapper.writeValueAsString(user));
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }

        return res;
    }

    public static Response createUser(Request req, Response res) throws Exception {
        try {
            final User user = mapper.readValue(req.body(), User.class);
            dbManager.createUser(user);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException e) {
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return res;
    }

    public static Response updateUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("id"));
        final User user = User.from(mapper.readValue(req.body(), User.class))
                .id(userId)
                .build();
        dbManager.updateUser(user);
        res.status(HttpStatus.OK_200);
        return res;
    }

    public static Response deleteUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("id"));
        dbManager.deleteUser(userId);
        return res;
    }
}
