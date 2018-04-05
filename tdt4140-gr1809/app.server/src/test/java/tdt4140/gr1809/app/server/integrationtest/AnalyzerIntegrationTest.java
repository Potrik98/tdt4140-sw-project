package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.*;

public class AnalyzerIntegrationTest {
    @BeforeClass
    public static void setupFilterTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testGeneratingNotificationsForHeartRate() throws Exception {
        final int maxPulse = 100;

        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .maxPulse(maxPulse)
                .build();
        userClient.createUser(user);

        final DataPoint dataPointHeartRateValueOverMaxPulse = DataPoint.builder()
                .dataType(DataPoint.DataType.HEART_RATE)
                .value(maxPulse + 10)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        final DataPoint dataPointHeartRateValueUnderMaxPulse = DataPoint.builder()
                .dataType(DataPoint.DataType.HEART_RATE)
                .value(maxPulse - 10)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        final DataPoint dataPointOtherDataTypeValueOverMaxPulse = DataPoint.builder()
                .dataType(DataPoint.DataType.STEPS)
                .value(maxPulse + 10)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        dataClient.createDataPoint(dataPointHeartRateValueOverMaxPulse);
        dataClient.createDataPoint(dataPointHeartRateValueUnderMaxPulse);
        dataClient.createDataPoint(dataPointOtherDataTypeValueOverMaxPulse);

        final List<Notification> notificationsForUser =
                notificationClient.getNotificationByUserId(user.getId());

        assertThat(notificationsForUser).hasSize(1);

        final Notification notification = notificationsForUser.get(0);

        assertThat(notification.getMessage()).isEqualTo("Pulse was over max-pulse at "
                + dataPointHeartRateValueOverMaxPulse.getTime()
                + ".\nValue: " + dataPointHeartRateValueOverMaxPulse.getValue());
    }
}
