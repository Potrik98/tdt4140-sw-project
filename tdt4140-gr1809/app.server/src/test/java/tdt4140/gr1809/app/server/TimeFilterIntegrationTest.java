package tdt4140.gr1809.app.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.client.ClientException;
import tdt4140.gr1809.app.client.TimeFilterClient;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;
import tdt4140.gr1809.app.server.resource.TimeFilterResource;
import tdt4140.gr1809.app.server.resource.UserResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class TimeFilterIntegrationTest {
    public static final int TEST_PORT = 8192;
    private static TimeFilterClient client;
    private static UserClient userClient;
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

        client = new TimeFilterClient("http://localhost:" + TEST_PORT);
        userClient = new UserClient("http://localhost:" + TEST_PORT);
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
    public void testCreateAndGetTimeFilters() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        userClient.createUser(user);

        final TimeFilter filter1 = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        client.createTimeFilter(filter1);

        final TimeFilter filter2 = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        client.createTimeFilter(filter2);

        List<TimeFilter> timeFilters = client.getTimeFiltersForUserId(user.getId());
        assertThat(timeFilters).usingFieldByFieldElementComparator()
                .containsExactly(filter1, filter2);
    }

    @Test
    public void testCreateInvalidTimeFilter() {
        final TimeFilter p1 = TimeFilter.builder()
                .userId(UUID.randomUUID())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> client.createTimeFilter(p1));
    }
}
