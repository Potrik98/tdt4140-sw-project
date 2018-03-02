package tdt4140.gr1809.app.server.resource;

import spark.Request;
import spark.Response;

import java.util.UUID;

public class UserResource {
    public static void init() {

    }

    public static Response getUserById(Request req, Response res) {
        UUID userId = UUID.fromString(req.params("id"));
        return res;
    }

    public static Response putUser(Request req, Response res) {
        UUID userId = UUID.fromString(req.params("id"));
        return res;
    }

    public static Response deleteUser(Request req, Response res) {
        UUID userId = UUID.fromString(req.params("id"));
        return res;
    }
}
