package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.customNotificationThresholdDBManager;
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
                + dataPointHeartRateValueOverMaxPulse.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
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
                "Low body temperature at " + dataPointTemperatureLowValue.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
                        + ".\nValue: " + dataPointTemperatureLowValue.getValue(),
                "High body temperature at " + dataPointTemperatureHighValue.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()
                        + ".\nValue: " + dataPointTemperatureHighValue.getValue()
        );
    }

    @Test
    public void testGeneratingNotificationsWithCustomNotificationThresholds() throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.STEPS;
        final DataPoint.DataType anotherDataType = DataPoint.DataType.HEART_RATE;
        final int lowValue = 10;
        final int highValue = 100;

        final User user = User.builder().build();
        userDBManager.createUser(user);

        final CustomNotificationThreshold customNotificationThresholdLowValue =
                CustomNotificationThreshold.builder()
                        .value(lowValue)
                        .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                        .dataType(dataType)
                        .userId(user.getId())
                        .message("Custom message low value")
                        .build();
        final CustomNotificationThreshold customNotificationThresholdHighValue =
                CustomNotificationThreshold.builder()
                        .value(highValue)
                        .thresholdType(CustomNotificationThreshold.ThresholdType.MORE_THAN)
                        .dataType(dataType)
                        .userId(user.getId())
                        .message("Custom message high value")
                        .build();

        customNotificationThresholdDBManager
                .createCustomNotificationThreshold(customNotificationThresholdLowValue);
        customNotificationThresholdDBManager
                .createCustomNotificationThreshold(customNotificationThresholdHighValue);

        final DataPoint dataPointLowValue = DataPoint.builder()
                .userId(user.getId())
                .dataType(dataType)
                .time(LocalDateTime.now())
                .value(lowValue - 10)
                .build();
        final DataPoint dataPointMediumValue = DataPoint.builder()
                .userId(user.getId())
                .dataType(dataType)
                .time(LocalDateTime.now())
                .value((lowValue + highValue) / 2)
                .build();
        final DataPoint dataPointHighValue = DataPoint.builder()
                .userId(user.getId())
                .dataType(dataType)
                .time(LocalDateTime.now())
                .value(highValue + 10)
                .build();
        final DataPoint dataPointOfAnotherDataType = DataPoint.builder()
                .userId(user.getId())
                .dataType(anotherDataType)
                .time(LocalDateTime.now())
                .value(80)
                .build();

        analyzer.analyzeDataPoints(ImmutableList.of(
                dataPointHighValue,
                dataPointMediumValue,
                dataPointLowValue,
                dataPointOfAnotherDataType
        ));

        final List<Notification> notificationsForUser =
                notificationDBManager.getNotificationByUserId(user.getId());

        assertThat(notificationsForUser).hasSize(2);

        final List<String> notificationMessages = notificationsForUser.stream()
                .map(Notification::getMessage)
                .collect(Collectors.toList());

        assertThat(notificationMessages).containsExactlyInAnyOrder(
                customNotificationThresholdHighValue.getMessage()
                        .concat("\nTime: " + dataPointHighValue.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                        .concat("\nValue: " + dataPointHighValue.getValue()),
                customNotificationThresholdLowValue.getMessage()
                        .concat("\nTime: " + dataPointLowValue.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString())
                        .concat("\nValue: " + dataPointLowValue.getValue())
        );
    }
}
