package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.core.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.*;

public class StatisticsIntegrationTest {
    @BeforeClass
    public static void setupStatisticTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testGetStatisticForDataType() throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.HEART_RATE;
        final DataPoint.DataType anotherDataType = DataPoint.DataType.STEPS;
        final int value1 = 100;
        final int value2 = 200;
        final int value3 = 300;
        final double average = (value1 + value2 + value3) / 3.0;

        final User user = User.builder().build();
        userClient.createUser(user);

        final DataPoint dataPoint1 = DataPoint.builder()
                .dataType(dataType)
                .userId(user.getId())
                .value(value1)
                .build();
        final DataPoint dataPoint2 = DataPoint.builder()
                .dataType(dataType)
                .userId(user.getId())
                .value(value2)
                .build();
        final DataPoint dataPoint3 = DataPoint.builder()
                .dataType(dataType)
                .userId(user.getId())
                .value(value3)
                .build();
        final DataPoint dataPointOfAnotherType = DataPoint.builder()
                .dataType(anotherDataType)
                .userId(user.getId())
                .value(123)
                .build();

        dataClient.createDataPoint(dataPoint1);
        dataClient.createDataPoint(dataPoint2);
        dataClient.createDataPoint(dataPoint3);
        dataClient.createDataPoint(dataPointOfAnotherType);

        final Statistic statistic = statisticsClient.getStatisticsForDataType(dataType);

        assertThat(statistic.getValue()).isEqualTo(average);
    }
}
