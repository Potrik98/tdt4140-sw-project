package tdt4140.gr1809.app.server;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import org.eclipse.jetty.http.HttpStatus;

import tdt4140.gr1809.app.server.resource.DataResource;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;
import tdt4140.gr1809.app.server.resource.ServiceProviderResource;

public class Server {
    public static void main(String[] args) throws Exception {
        startServer(80);
        UserResource.init();
        TimeFilterResource.init();
        DataResource.init();
        ServiceProviderResource.init();
    }

    public static void startServer(int port) throws Exception {
        port(port);
        System.out.println("Starting server...");

        path("/user", () -> {
            post("/:id", UserResource::updateUser);
            post("", UserResource::createUser);
            get("/:id", UserResource::getUserById);
            delete("/:id", UserResource::deleteUser);
            get("/:id/datapoints", UserResource::getDataPointsOfUser);
            get("/:id/timefilters", UserResource::getTimeFiltersOfUser);
        });
        path("/datapoints", () -> {
            post("", DataResource::createDataPoint);
        });
        path( "/timefilters", () -> {
            post("", TimeFilterResource::createTimeFilter);
        });
        path("/serviceprovider", () -> {
        		get("/:id", ServiceProviderResource::getServiceProviderById);
        		post("", ServiceProviderResource::createServiceProvider);
        		delete("/:id", ServiceProviderResource::deleteServiceProvider);
        		post("/:id", ServiceProviderResource::updateServiceProvider);
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
