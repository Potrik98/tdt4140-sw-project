package tdt4140.gr1809.app.server;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import org.eclipse.jetty.http.HttpStatus;

import tdt4140.gr1809.app.server.resource.AccessResource;
import tdt4140.gr1809.app.server.resource.DataResource;
import tdt4140.gr1809.app.server.resource.NotificationResource;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;
import tdt4140.gr1809.app.server.resource.ServiceProviderResource;

public class Server {
    public static void main(String[] args) throws Exception {
        startServer(80);
        UserResource.init();
        TimeFilterResource.init();
        AccessResource.init();
        DataResource.init();
        ServiceProviderResource.init();
    }

    public static void startServer(int port) throws Exception {
        port(port);
        System.out.println("Starting server...");

        path("/user", () -> {
            post("", UserResource::createUser);
            path("/:userId", () -> {
                post("", UserResource::updateUser);
                get("", UserResource::getUserById);
                delete("", UserResource::deleteUser);
                get("/datapoints", UserResource::getDataPointsOfUser);
                get("/timefilters", UserResource::getTimeFiltersOfUser);
                get("/notifications", UserResource::getNotificationsOfUser);

                path("/serviceproviders", () -> {
                    get("",
                            AccessResource::getServiceProvidersWithAccessToUser);
                    post("/:serviceProviderId",
                            AccessResource::giveServiceProviderAccessToUser);
                    delete("/:serviceProviderId",
                            AccessResource::revokeServiceProviderAccessToUser);
                });
            });
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
        path("/notifications", () -> {
        	post("", NotificationResource::createNotification);
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
