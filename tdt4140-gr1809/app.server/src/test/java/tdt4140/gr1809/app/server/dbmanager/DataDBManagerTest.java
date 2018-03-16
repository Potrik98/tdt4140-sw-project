package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DataDBManagerTest {
    private static DataDBManager dataDBManager;
    private static UserDBManager userDBManager;

    @BeforeClass
    public static void openConnection() throws Exception {
        dataDBManager = new DataDBManager();
        userDBManager = new UserDBManager();
        DBManager.loadCreateScript();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        dataDBManager.closeConnection();
        userDBManager.closeConnection();
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

        dataDBManager.createDataPoint(data);

        final List<DataPoint> retrievedData = dataDBManager.getDataByUserId(user.getId());

        assertThat(retrievedData).usingFieldByFieldElementComparator()
                .containsExactly(data);
    }

    @Test
    public void testGetDataInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final List<DataPoint> retrievedData = dataDBManager.getDataByUserId(invalidUserId);

        assertThat(retrievedData).isEmpty();
    }
}
