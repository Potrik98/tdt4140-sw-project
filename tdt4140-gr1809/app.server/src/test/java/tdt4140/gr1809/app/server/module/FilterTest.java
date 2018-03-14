package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.TimeFilterDBManager;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterTest {
    private static Connection connection;
    private static UserDBManager userDBManager;
    private static TimeFilterDBManager timeFilterDBManager;
    private static Filter filter;

    @BeforeClass
    public static void openConnection() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        InputStream input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        Statement statement = connection.createStatement();
        statement.execute(out.toString());

        System.out.println("Successfully loaded test db");

        timeFilterDBManager = new TimeFilterDBManager(connection);
        userDBManager = new UserDBManager(connection);
        filter = new Filter(timeFilterDBManager);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void testFilterExpiredFutureAndCurrent() throws Exception {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userDBManager.createUser(user);

        final TimeFilter filterTemperature = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        timeFilterDBManager.createTimeFilter(filterTemperature);
        assertThat(timeFilterDBManager.getTimeFiltersByUserId(user.getId())).usingFieldByFieldElementComparator().containsExactly(filterTemperature);
        final TimeFilter filterHeartRateExpired = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(10, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().minus(5, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterDBManager.createTimeFilter(filterHeartRateExpired);
        final TimeFilter filterStepsFuture = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.STEPS)
                .build();
        timeFilterDBManager.createTimeFilter(filterStepsFuture);

        final DataPoint currentTemperature = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .time(LocalDateTime.now())
                .value(123)
                .build();
        final DataPoint oldTemperature = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .time(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                .value(123)
                .build();
        final DataPoint currentSteps = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.STEPS)
                .time(LocalDateTime.now())
                .value(123)
                .build();
        final DataPoint currentHeartRate = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now())
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
                .build();
        userDBManager.createUser(user1);
        final User user2 = User.builder()
                .firstName("User")
                .lastName("Two")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userDBManager.createUser(user2);
        final TimeFilter filterHeartRateForUser2 = TimeFilter.builder()
                .userId(user2.getId())
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterDBManager.createTimeFilter(filterHeartRateForUser2);
        final DataPoint currentHeartRateForUser1 = DataPoint.builder()
                .userId(user1.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now())
                .value(123)
                .build();
        final DataPoint currentHeartRateForUser2 = DataPoint.builder()
                .userId(user2.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now())
                .value(123)
                .build();

        final List<DataPoint> dataPoints = ImmutableList
                .of(currentHeartRateForUser1, currentHeartRateForUser2);

        final List<DataPoint> filteredDataPoints = filter.filterDataPoints(dataPoints);

        assertThat(filteredDataPoints).containsExactly(currentHeartRateForUser1);
    }
}
