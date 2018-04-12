package tdt4140.gr1809.app.server.integrationtest;

import com.google.common.collect.ImmutableList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.client.ClientException;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.dataClient;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.userClient;

public class DataIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testCreateAndGetDatapoints() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        final DataPoint p1 = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(100)
                .build();
        dataClient.createDataPoint(p1);

        final DataPoint p2 = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(100)
                .build();
        dataClient.createDataPoint(p2);

        List<DataPoint> dataPoints = dataClient.getDataPointsForUserId(user.getId());
        assertThat(dataPoints).usingFieldByFieldElementComparator()
                .containsExactly(p1, p2);
    }

    @Test
    public void testCreateInvalidDatapoint() {
        final DataPoint p1 = DataPoint.builder()
                .dataType(DataPoint.DataType.HEART_RATE)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(100)
                .build();
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> dataClient.createDataPoint(p1));
    }

    @Test
    public void testCreateDataPointList() {
        final User user1 = User.builder().build();
        userClient.createUser(user1);

        final User user2 = User.builder().build();
        userClient.createUser(user2);

        final List<DataPoint> dataPoints = ImmutableList.<DataPoint>builder()
                .add(DataPoint.builder()
                        .userId(user1.getId())
                        .dataType(DataPoint.DataType.HEART_RATE)
                        .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                        .value(100)
                        .build())
                .add(DataPoint.builder()
                        .userId(user1.getId())
                        .dataType(DataPoint.DataType.HEART_RATE)
                        .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                        .value(100)
                        .build())
                .add(DataPoint.builder()
                        .userId(user2.getId())
                        .dataType(DataPoint.DataType.HEART_RATE)
                        .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                        .value(100)
                        .build())
                .build();

        dataClient.createDataPoints(dataPoints);

        final List<DataPoint> retrievedDataPointsUser1 = dataClient.getDataPointsForUserId(user1.getId());
        final List<DataPoint> retrievedDataPointsUser2 = dataClient.getDataPointsForUserId(user2.getId());
        final List<DataPoint> allRetrievedDataPoints = ImmutableList.<DataPoint>builder()
                .addAll(retrievedDataPointsUser1)
                .addAll(retrievedDataPointsUser2)
                .build();

        assertThat(dataPoints).usingFieldByFieldElementComparator()
                .containsAll(retrievedDataPointsUser1);
        assertThat(dataPoints).usingFieldByFieldElementComparator()
                .containsAll(retrievedDataPointsUser2);
        assertThat(allRetrievedDataPoints).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(dataPoints);
    }
}
