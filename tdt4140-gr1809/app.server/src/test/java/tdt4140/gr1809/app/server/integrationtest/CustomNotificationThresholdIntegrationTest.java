package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.customNotificationThresholdClient;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.notificationClient;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.userClient;

public class CustomNotificationThresholdIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testCreateAndGetCustomNotificationThreshold() throws Exception {
        final User user = User.builder().build();
        final User anotherUser = User.builder().build();
        userClient.createUser(user);
        userClient.createUser(anotherUser);

        final CustomNotificationThreshold customNotificationThreshold1 = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(40)
                .message("message")
                .build();
        final CustomNotificationThreshold customNotificationThreshold2 = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.STEPS)
                .thresholdType(CustomNotificationThreshold.ThresholdType.MORE_THAN)
                .value(20)
                .message("message")
                .build();
        final CustomNotificationThreshold customNotificationThresholdForAnotherUser =
                CustomNotificationThreshold.builder()
                        .userId(anotherUser.getId())
                        .dataType(DataPoint.DataType.TEMPERATURE)
                        .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                        .value(30)
                        .message("message")
                        .build();

        customNotificationThresholdClient.createCustomNotificationThreshold(customNotificationThreshold1);
        customNotificationThresholdClient.createCustomNotificationThreshold(customNotificationThreshold2);
        customNotificationThresholdClient
                .createCustomNotificationThreshold(customNotificationThresholdForAnotherUser);

        final List<CustomNotificationThreshold> retrievedCustomNotificationThresholds =
                customNotificationThresholdClient.getCustomNotificationThresholdsForUserId(user.getId());

        assertThat(retrievedCustomNotificationThresholds).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(customNotificationThreshold1, customNotificationThreshold2);
    }

    @Test
    public void testDeleteCustomNotificationThreshold() throws Exception {
        final User user = User.builder().build();
        userClient.createUser(user);

        final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(40)
                .message("message")
                .build();

        customNotificationThresholdClient.createCustomNotificationThreshold(customNotificationThreshold);

        customNotificationThresholdClient.deleteCustomNotificationThreshold(customNotificationThreshold.getId());

        final List<CustomNotificationThreshold> customNotificationThresholdsForUser =
                customNotificationThresholdClient.getCustomNotificationThresholdsForUserId(user.getId());

        assertThat(customNotificationThresholdsForUser).isEmpty();
    }
}
