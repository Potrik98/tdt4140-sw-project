package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.User;

import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.accessDBManager;

public class AccessResource {
    public static String giveServiceProviderAccessToUser(Request req, Response res) throws Exception {
        System.out.println("Give sp access to user");
        UUID serviceProviderId = UUID.fromString(req.params("serviceProviderId"));
        UUID userId = UUID.fromString(req.params("userId"));
        accessDBManager.giveServiceProviderAccessToUser(serviceProviderId, userId);
        res.status(HttpStatus.CREATED_201);
        return "";
    }

    public static String getServiceProvidersWithAccessToUser(Request req, Response res) throws Exception {
        UUID userId = UUID.fromString(req.params("userId"));
        System.out.println("Get sps with access to user " + userId.toString());
        res.status(HttpStatus.OK_200);
        res.type("application/json");
        return User.mapper.writeValueAsString(accessDBManager
                .getServiceProvidersWithAccessToUser(userId));
    }

    public static String getUsersServiceProviderHasAccessTo(Request req, Response res) throws Exception {
        UUID serviceProviderId = UUID.fromString(req.params("serviceProviderId"));
        System.out.println("Get users for sp " + serviceProviderId.toString());
        res.status(HttpStatus.OK_200);
        res.type("application/json");
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
