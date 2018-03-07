package tdt4140.gr1809.app.server;

import org.eclipse.jetty.http.HttpStatus;
import tdt4140.gr1809.app.server.resource.UserResource;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) throws Exception {
        startServer(80);
        UserResource.init();
    }

    public static void startServer(int port) throws Exception {
        port(port);
        System.out.println("Starting server...");

        path("/user", () -> {
            post("/:id", UserResource::updateUser);
            post("", UserResource::createUser);
            get("/:id", UserResource::getUserById);
            delete("/:id", UserResource::deleteUser);
        });

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.type("application/json");
            response.body("{\"message\":\"" + exception.getMessage() + "\"}");
        });
    }

    public static void stopServer() {
        stop();
    }
}
