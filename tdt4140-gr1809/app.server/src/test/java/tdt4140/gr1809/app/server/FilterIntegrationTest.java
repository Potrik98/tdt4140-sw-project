package tdt4140.gr1809.app.server;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.client.DataClient;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.LoadDBManagerTest;
import tdt4140.gr1809.app.server.resource.DataResource;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.dataClient;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.timeFilterClient;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.userClient;

public class FilterIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testFilterExpiredFutureAndCurrent() throws Exception {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userClient.createUser(user);

        final TimeFilter filterTemperature = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        timeFilterClient.createTimeFilter(filterTemperature);
        final TimeFilter filterHeartRateExpired = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(10, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().minus(5, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterClient.createTimeFilter(filterHeartRateExpired);
        final TimeFilter filterStepsFuture = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(10, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.STEPS)
                .build();
        timeFilterClient.createTimeFilter(filterStepsFuture);

        final DataPoint currentTemperature = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();
        final DataPoint oldTemperature = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .time(LocalDateTime.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();
        final DataPoint currentSteps = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.STEPS)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();
        final DataPoint currentHeartRate = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();

        final List<DataPoint> dataPoints = ImmutableList
                .of(currentTemperature, oldTemperature, currentSteps, currentHeartRate);
        dataPoints.forEach(dataClient::createDataPoint);

        final List<DataPoint> filteredDataPoints = dataClient.getDataPointsForUserId(user.getId());

        assertThat(filteredDataPoints).usingFieldByFieldElementComparator()
                .containsExactly(oldTemperature, currentSteps, currentHeartRate);
    }

    @Test
    public void testFilterMultipleUsers() throws Exception {
        final User user1 = User.builder()
                .firstName("User")
                .lastName("One")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userClient.createUser(user1);
        final User user2 = User.builder()
                .firstName("User")
                .lastName("Two")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userClient.createUser(user2);
        final TimeFilter filterHeartRateForUser2 = TimeFilter.builder()
                .userId(user2.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterClient.createTimeFilter(filterHeartRateForUser2);
        final DataPoint currentHeartRateForUser1 = DataPoint.builder()
                .userId(user1.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();
        final DataPoint currentHeartRateForUser2 = DataPoint.builder()
                .userId(user2.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(123)
                .build();

        final List<DataPoint> dataPoints = ImmutableList
                .of(currentHeartRateForUser1, currentHeartRateForUser2);
        dataPoints.forEach(dataClient::createDataPoint);

        final List<DataPoint> dataPointsUser1 = dataClient.getDataPointsForUserId(user1.getId());

        assertThat(dataPointsUser1).usingFieldByFieldElementComparator()
                .containsExactly(currentHeartRateForUser1);

        final List<DataPoint> dataPointsUser2 = dataClient.getDataPointsForUserId(user2.getId());
        assertThat(dataPointsUser2).isEmpty();
    }
}
