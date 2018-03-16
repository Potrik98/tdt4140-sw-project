package tdt4140.gr1809.app.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.client.ClientException;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.timeFilterClient;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.userClient;

public class TimeFilterIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
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
                .startTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        timeFilterClient.createTimeFilter(filter1);

        final TimeFilter filter2 = TimeFilter.builder()
                .userId(user.getId())
                .startTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        timeFilterClient.createTimeFilter(filter2);

        List<TimeFilter> timeFilters = timeFilterClient.getTimeFiltersForUserId(user.getId());
        assertThat(timeFilters).usingFieldByFieldElementComparator()
                .containsExactly(filter1, filter2);
    }

    @Test
    public void testCreateInvalidTimeFilter() {
        final TimeFilter p1 = TimeFilter.builder()
                .userId(UUID.randomUUID())
                .startTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .endTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .dataType(DataPoint.DataType.TEMPERATURE)
                .build();
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> timeFilterClient.createTimeFilter(p1));
    }
}
