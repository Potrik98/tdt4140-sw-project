package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DataDBManagerTest {
    private static DataDBManager dbManager;
    private static UserDBManager userDBManager;
    private static Connection connection;

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

        dbManager = new DataDBManager(connection);
        userDBManager = new UserDBManager(connection);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void testCreateAndGetData() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userDBManager.createUser(user);
        final DataPoint data = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .value(123)
                .time(LocalDateTime.now())
                .build();

        dbManager.createDataPoint(data);

        final List<DataPoint> retrievedData = dbManager.getDataByUserId(user.getId());

        assertThat(retrievedData).usingFieldByFieldElementComparator()
                .containsExactly(data);
    }

    @Test
    public void testGetDataInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final List<DataPoint> retrievedData = dbManager.getDataByUserId(invalidUserId);

        assertThat(retrievedData).isEmpty();
    }
}
