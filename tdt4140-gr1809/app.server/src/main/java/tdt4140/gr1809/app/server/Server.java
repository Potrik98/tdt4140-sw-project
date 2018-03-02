package tdt4140.gr1809.app.server;

import tdt4140.gr1809.app.server.resource.UserResource;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        port(80);
        System.out.println("Starting server...");
        path("/user", () -> {
            get("/:id", UserResource::getUserById);
            put("/:id", UserResource::putUser);
            post("/:id", UserResource::putUser);
            delete("/:id", UserResource::deleteUser);
        });
    }
}
