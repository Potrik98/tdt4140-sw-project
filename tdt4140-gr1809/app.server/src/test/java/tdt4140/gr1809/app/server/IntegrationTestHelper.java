package tdt4140.gr1809.app.server;

import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.client.ServiceProviderClient;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.server.dbmanager.DBManager;
import tdt4140.gr1809.app.server.resource.AccessResource;
import tdt4140.gr1809.app.server.resource.DataResource;
import tdt4140.gr1809.app.server.resource.NotificationResource;
import tdt4140.gr1809.app.server.resource.ServiceProviderResource;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;

public class IntegrationTestHelper {
    public static final int TEST_PORT = 8192;
    public static DataClient dataClient;
    public static UserClient userClient;
    public static TimeFilterClient timeFilterClient;
    public static ServiceProviderClient serviceProviderClient;

    public static void setupIntegrationTest() throws Exception {
        Server.startServer(TEST_PORT);

        UserResource.init();
        TimeFilterResource.init();
        DataResource.init();
        ServiceProviderResource.init();
        AccessResource.init();
        NotificationResource.init();

        DBManager.loadCreateScript();

        dataClient = new DataClient();
        userClient = new UserClient();
        timeFilterClient = new TimeFilterClient();
        serviceProviderClient = new ServiceProviderClient();
    }

    public static void stopIntegrationTest() throws Exception {
        UserResource.closeConnection();
        TimeFilterResource.closeConnection();
        DataResource.closeConnection();
        ServiceProviderResource.closeConnection();
        AccessResource.closeConnection();
        NotificationResource.closeConnection();

        Server.stopServer();
        // Remove when https://github.com/perwendel/spark/issues/705 is fixed.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
