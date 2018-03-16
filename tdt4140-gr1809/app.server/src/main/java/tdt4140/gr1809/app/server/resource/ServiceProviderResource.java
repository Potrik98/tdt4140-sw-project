package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.server.dbmanager.ServiceProviderDBManager;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public class ServiceProviderResource {
    private static ServiceProviderDBManager dbManager;
    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    public static void init() throws Exception {
        dbManager = new ServiceProviderDBManager();
    }

    public static void init(Connection connection) {
        dbManager = new ServiceProviderDBManager(connection);
    }

    public static String getServiceProviderById(Request req, Response res) throws Exception {
        System.out.println("recieved request");
        log.error("recieved request");
        UUID serviceProviderId = UUID.fromString(req.params("id"));
        System.out.println("Get serviceProviderId: " + serviceProviderId.toString());
        Optional<ServiceProvider> serviceProvider = dbManager.getServiceProviderById(serviceProviderId);

        if (serviceProvider.isPresent()) {
            res.type("application/json");
            res.status(HttpStatus.OK_200);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }

        return ServiceProvider.mapper.writeValueAsString(serviceProvider);
    }

    public static String createServiceProvider(Request req, Response res) throws Exception {
        try {
            final ServiceProvider serviceProvider = ServiceProvider.mapper.readValue(req.body(), ServiceProvider.class);
            System.out.println("Create: Recieved service provider with id : " + serviceProvider.getId());
            dbManager.createServiceProvider(serviceProvider);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }

    public static String updateServiceProvider(Request req, Response res) throws Exception {
        UUID serviceProviderId = UUID.fromString(req.params("id"));
        System.out.println("Update: recieved service provider with id: " + serviceProviderId);
        final ServiceProvider serviceProvider = ServiceProvider.from(ServiceProvider.mapper.readValue(req.body(), ServiceProvider.class))
                .id(serviceProviderId)
                .build();
        dbManager.updateServiceProvider(serviceProvider);
        res.status(HttpStatus.OK_200);
        return "";
    }

    public static String deleteServiceProvider(Request req, Response res) throws Exception {
        UUID serviceProviderId = UUID.fromString(req.params("id"));
        System.out.println("Delete serviceProviderId: " + serviceProviderId);
        if (dbManager.deleteServiceProvider(serviceProviderId)) {
            res.status(HttpStatus.NO_CONTENT_204);
        } else {
            res.status(HttpStatus.NOT_FOUND_404);
        }
        return "";
    }
}

