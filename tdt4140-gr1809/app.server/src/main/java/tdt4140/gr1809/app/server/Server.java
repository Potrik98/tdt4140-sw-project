package tdt4140.gr1809.app.server;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.stop;

import org.eclipse.jetty.http.HttpStatus;

import tdt4140.gr1809.app.server.dbmanager.DBManager;
import tdt4140.gr1809.app.server.resource.*;

public class Server {
    public static void main(String[] args) throws Exception {
        startServer(80);
    }

    public static void startServer(int port) throws Exception {
        port(port);
        DBManager.initDBManagers();
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
            delete("/:timeFilterId", TimeFilterResource::deleteTimefilter);
        });
        path("/serviceprovider", () -> {
        		post("", ServiceProviderResource::createServiceProvider);
        		path("/:serviceProviderId", () -> {
                    get("", ServiceProviderResource::getServiceProviderById);
                    delete("", ServiceProviderResource::deleteServiceProvider);
                    post("", ServiceProviderResource::updateServiceProvider);
                    get("/users", AccessResource::getUsersServiceProviderHasAccessTo);
                });
        });
        path("/notifications", () -> {
        	post("", NotificationResource::createNotification);
        	path("/:notificationId", () -> {
        		delete("", NotificationResource::deleteNotification);
        	});
        });
        path("/statistics", () -> {
            get("/:dataType", StatisticsResource::getStatisticsForDataType);
        });

        exception(IllegalArgumentException.class, (exception, request, response) -> {
            response.status(HttpStatus.BAD_REQUEST_400);
            response.type("application/json");
            response.body("{\"message\":\"" + exception.getMessage() + "\"}");
        });

        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            response.type("application/json");
            response.body("{\"message\":\"" + exception.getMessage() + "\"}");
        });
    }

    public static void stopServer() throws Exception {
        DBManager.closeDBConnections();
        stop();
    }
}
