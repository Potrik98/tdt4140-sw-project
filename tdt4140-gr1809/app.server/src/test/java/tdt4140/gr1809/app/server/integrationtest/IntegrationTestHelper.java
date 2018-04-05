package tdt4140.gr1809.app.server.integrationtest;

import tdt4140.gr1809.app.client.*;
import tdt4140.gr1809.app.server.Server;
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
    public static NotificationClient notificationClient;
    public static TimeFilterClient timeFilterClient;
    public static ServiceProviderClient serviceProviderClient;
    public static AccessClient accessClient;
    public static StatisticsClient statisticsClient;

    public static void setupIntegrationTest() throws Exception {
        Server.startServer(TEST_PORT);

        DBManager.loadCreateScript();

        dataClient = new DataClient();
        userClient = new UserClient();
        timeFilterClient = new TimeFilterClient();
        serviceProviderClient = new ServiceProviderClient();
        notificationClient = new NotificationClient();
        accessClient = new AccessClient();
        statisticsClient = new StatisticsClient();
    }

    public static void stopIntegrationTest() throws Exception {
        Server.stopServer();
        // Remove when https://github.com/perwendel/spark/issues/705 is fixed.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
