package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.IntegrationTestHelper;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.timeFilterDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class FilterTest {
    private static Filter filter;

    @BeforeClass
    public static void setupFilterTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
        filter = new Filter();
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
                .maxPulse(123)
                .build();
        userDBManager.createUser(user);

        final TimeFilter filterTemperature = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        timeFilterDBManager.createTimeFilter(filterTemperature);
        assertThat(timeFilterDBManager.getTimeFiltersByUserId(user.getId())).usingFieldByFieldElementComparator()
                .containsExactly(filterTemperature);
        final TimeFilter filterHeartRateExpired = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(10, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().minus(5, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterDBManager.createTimeFilter(filterHeartRateExpired);
        final TimeFilter filterStepsFuture = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(10, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.STEPS)
                .build();
        timeFilterDBManager.createTimeFilter(filterStepsFuture);

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

        final List<DataPoint> filteredDataPoints = filter.filterDataPoints(dataPoints);

        assertThat(filteredDataPoints).containsExactly(oldTemperature, currentSteps, currentHeartRate);
    }

    @Test
    public void testFilterMultipleUsers() throws Exception {
        final User user1 = User.builder()
                .firstName("User")
                .lastName("One")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .build();
        userDBManager.createUser(user1);
        final User user2 = User.builder()
                .firstName("User")
                .lastName("Two")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .build();
        userDBManager.createUser(user2);
        final TimeFilter filterHeartRateForUser2 = TimeFilter.builder()
                .userId(user2.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS).truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterDBManager.createTimeFilter(filterHeartRateForUser2);
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

        final List<DataPoint> filteredDataPoints = filter.filterDataPoints(dataPoints);

        assertThat(filteredDataPoints).containsExactly(currentHeartRateForUser1);
    }
}
