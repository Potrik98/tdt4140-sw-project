package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.User;

import java.sql.Connection;
import java.util.UUID;

public class AccessResource {
    protected AccessTableDBManager accessDBManager;

    public static void init() throws Exception {
        accessDBManager = new AccessTableDBManager();
    }

    public static void init(Connection connection) {
        accessDBManager = new AccessTableDBManager(connection);
    }

    public static String giveServiceProviderAccessToUser(Request req, Response res) throws Exception {
        System.out.println("Give sp access to user");
        UUID serviceProviderId = UUID.fromString(req.params("serviceProviderId"));
        UUID userId = UUID.fromString(req.params("userId"));
        accessDBManager.giveServiceProviderAccessToUser(serviceProviderId, userId);
        res.status(HttpStatus.CREATED_201);
        return "";
    }

    public static String getServiceProvidersWithAccessToUser(Request req, Response res) throws Exception {
        System.out.println("Get sps with access to user");
        UUID userId = UUID.fromString(req.params("userId"));
        res.status(HttpStatus.OK_200);
        return User.mapper.writeValueAsString(accessDBManager
                .getServiceProvidersWithAccessToUser(userId));
    }

    public static String getUsersServiceProviderHasAccessTo(Request req, Response res) throws Exception {
        System.out.println("Get users for sp");
        UUID serviceProviderId = UUID.fromString(req.params("serviceProviderId"));
        res.status(HttpStatus.OK_200);
        return User.mapper.writeValueAsString(accessDBManager
                .getUsersServiceProviderHasAccessTo(serviceProviderId));
    }

    public static String revokeServiceProviderAccessToUser(Request req, Response res) throws Exception {
        System.out.println("Revoke sp access to user");
        UUID serviceProviderId = UUID.fromString(req.params("serviceProviderId"));
        UUID userId = UUID.fromString(req.params("userId"));
        accessDBManager.revokeServiceProviderAccessToUser(serviceProviderId, userId);
        res.status(HttpStatus.NO_CONTENT_204);
        return "";
    }
}
