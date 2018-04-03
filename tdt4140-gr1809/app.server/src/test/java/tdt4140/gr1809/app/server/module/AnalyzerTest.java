package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.IntegrationTestHelper;
import tdt4140.gr1809.app.server.resource.NotificationResource;
import tdt4140.gr1809.app.server.resource.UserResource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyzerTest {
    private static Analyzer analyzer;

    @BeforeClass
    public static void setupFilterTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
        analyzer = new Analyzer(UserResource.dbManager, NotificationResource.dbManager);
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
        UserResource.dbManager.createUser(user);

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

        analyzer.analyzeDataPoints(ImmutableList.of(
                dataPointHeartRateValueOverMaxPulse,
                dataPointHeartRateValueUnderMaxPulse,
                dataPointOtherDataTypeValueOverMaxPulse));

        final List<Notification> notificationsForUser =
                NotificationResource.dbManager.getNotificationByUserId(user.getId());

        assertThat(notificationsForUser).hasSize(1);

        final Notification notification = notificationsForUser.get(0);

        assertThat(notification.getMessage()).isEqualTo("Pulse was over max-pulse at "
                + dataPointHeartRateValueOverMaxPulse.getTime()
                + ".\nValue: " + dataPointHeartRateValueOverMaxPulse.getValue());
    }
}
