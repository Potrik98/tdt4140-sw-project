package tdt4140.gr1809.app.server;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) {
        port(80);
        System.out.println("Starting server...");
        get("/hello", (req, res) -> "Hello");
    }
}
