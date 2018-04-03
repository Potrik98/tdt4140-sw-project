package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.IntegrationTestHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.notificationDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class AnalyzerTest {
    private static Analyzer analyzer;

    @BeforeClass
    public static void setupFilterTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
        analyzer = new Analyzer();
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
        userDBManager.createUser(user);

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
                notificationDBManager.getNotificationByUserId(user.getId());

        assertThat(notificationsForUser).hasSize(1);

        final Notification notification = notificationsForUser.get(0);

        assertThat(notification.getMessage()).isEqualTo("Pulse was over max-pulse at "
                + dataPointHeartRateValueOverMaxPulse.getTime()
                + ".\nValue: " + dataPointHeartRateValueOverMaxPulse.getValue());
    }

    @Test
    public void testGeneratingNotificationsForTemperature() throws Exception {
        final int lowTemperature = 3750;
        final int highTemperature = 3800;

        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        userDBManager.createUser(user);

        final DataPoint dataPointTemperatureHighValue = DataPoint.builder()
                .dataType(DataPoint.DataType.TEMPERATURE)
                .value(highTemperature + 10)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        final DataPoint dataPointTemperatureLowValue = DataPoint.builder()
                .dataType(DataPoint.DataType.TEMPERATURE)
                .value(lowTemperature - 10)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        final DataPoint dataPointTemperatureMediumValue = DataPoint.builder()
                .dataType(DataPoint.DataType.TEMPERATURE)
                .value((lowTemperature + highTemperature) / 2)
                .time(LocalDateTime.now())
                .userId(user.getId())
                .build();

        analyzer.analyzeDataPoints(ImmutableList.of(
                dataPointTemperatureHighValue,
                dataPointTemperatureLowValue,
                dataPointTemperatureMediumValue));

        final List<Notification> notificationsForUser =
                notificationDBManager.getNotificationByUserId(user.getId());

        assertThat(notificationsForUser).hasSize(2);

        final List<String> notificationMessages = notificationsForUser.stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());

        assertThat(notificationMessages).containsExactlyInAnyOrder(
                "Low body temperature at " + dataPointTemperatureLowValue.getTime()
                        + ".\nValue: " + dataPointTemperatureLowValue.getValue(),
                "High body temperature at " + dataPointTemperatureHighValue.getTime()
                        + ".\nValue: " + dataPointTemperatureHighValue.getValue()
        );
    }
}
