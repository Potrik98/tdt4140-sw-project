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

public class FilterIntegrationTest {
    public static final int TEST_PORT = 8192;
    private static TimeFilterClient timeFilterClient;
    private static UserClient userClient;
    private static DataClient dataClient;
    private static Connection connection;

    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        Server.startServer(TEST_PORT);

        // Open connection to local h2 db in memory
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        // Read sql test create script
        InputStream input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        // Execute sql test create script
        Statement statement = connection.createStatement();
        statement.execute(out.toString());

        // Init resources with db connection
        UserResource.init(connection);
        TimeFilterResource.init(connection);
        DataResource.init(connection);

        timeFilterClient = new TimeFilterClient("http://localhost:" + TEST_PORT);
        userClient = new UserClient("http://localhost:" + TEST_PORT);
        dataClient = new DataClient("http://localhost:" + TEST_PORT);
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        connection.close();
        Server.stopServer();
        // Remove when https://github.com/perwendel/spark/issues/705 is fixed.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        timeFilterClient.createTimeFilter(filterTemperature);
        final TimeFilter filterHeartRateExpired = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().minus(10, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().minus(5, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterClient.createTimeFilter(filterHeartRateExpired);
        final TimeFilter filterStepsFuture = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.STEPS)
                .build();
        timeFilterClient.createTimeFilter(filterStepsFuture);

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
                .startTime(LocalDateTime.now().minus(1, ChronoUnit.HOURS))
                .endTime(LocalDateTime.now().plus(1, ChronoUnit.HOURS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterClient.createTimeFilter(filterHeartRateForUser2);
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
        dataPoints.forEach(dataClient::createDataPoint);

        final List<DataPoint> dataPointsUser1 = dataClient.getDataPointsForUserId(user1.getId());

        assertThat(dataPointsUser1).usingFieldByFieldElementComparator()
                .containsExactly(currentHeartRateForUser1);

        final List<DataPoint> dataPointsUser2 = dataClient.getDataPointsForUserId(user2.getId());
        assertThat(dataPointsUser2).isEmpty();
    }
}
