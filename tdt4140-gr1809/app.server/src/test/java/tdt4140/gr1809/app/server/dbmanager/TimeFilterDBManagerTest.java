package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeFilterDBManagerTest {
    private static TimeFilterDBManager timeFilterDBManager;
    private static UserDBManager userDBManager;

    @BeforeClass
    public static void openConnection() throws Exception {
        timeFilterDBManager = new TimeFilterDBManager();
        userDBManager = new UserDBManager();
        DBManager.loadCreateScript();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        timeFilterDBManager.closeConnection();
        userDBManager.closeConnection();
    }

    @Test
    public void testCreateAndGetTimeFilter() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .build();
        userDBManager.createUser(user);
        final TimeFilter timeFilter = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();

        timeFilterDBManager.createTimeFilter(timeFilter);

        final List<TimeFilter> retrievedTimeFilter = timeFilterDBManager.getTimeFiltersByUserId(user.getId());

        assertThat(retrievedTimeFilter).usingFieldByFieldElementComparator()
                .containsExactly(timeFilter);
    }

    @Test
    public void testGetTimeFilterInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final List<TimeFilter> retrievedTimeFilter = timeFilterDBManager.getTimeFiltersByUserId(invalidUserId);

        assertThat(retrievedTimeFilter).isEmpty();
    }

    @Test
    public void testDeleteTimeFilter() throws SQLException {
        final User user = User.builder().build();
        userDBManager.createUser(user);

        final TimeFilter timeFilter = TimeFilter.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();
        timeFilterDBManager.createTimeFilter(timeFilter);

        timeFilterDBManager.deleteTimeFilter(timeFilter.getId());

        final List<TimeFilter> timeFiltersForUser =
                timeFilterDBManager.getTimeFiltersByUserId(user.getId());

        assertThat(timeFiltersForUser).isEmpty();
    }
}
