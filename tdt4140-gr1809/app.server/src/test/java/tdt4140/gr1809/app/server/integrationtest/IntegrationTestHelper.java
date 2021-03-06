package tdt4140.gr1809.app.server.integrationtest;

import tdt4140.gr1809.app.client.AccessClient;
import tdt4140.gr1809.app.client.CustomNotificationThresholdClient;
import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.client.NotificationClient;
import tdt4140.gr1809.app.client.ServiceProviderClient;
import tdt4140.gr1809.app.client.StatisticsClient;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.server.Server;
import tdt4140.gr1809.app.server.dbmanager.DBManager;

public class IntegrationTestHelper {
    public static final int TEST_PORT = 8192;
    public static DataClient dataClient;
    public static UserClient userClient;
    public static NotificationClient notificationClient;
    public static TimeFilterClient timeFilterClient;
    public static ServiceProviderClient serviceProviderClient;
    public static AccessClient accessClient;
    public static StatisticsClient statisticsClient;
    public static CustomNotificationThresholdClient customNotificationThresholdClient;

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
        customNotificationThresholdClient = new CustomNotificationThresholdClient();
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
