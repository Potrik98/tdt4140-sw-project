package tdt4140.gr1809.app.server.module;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.dataDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class StatisticsTest {

    @Before
    public void setupStatisticTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @After
    public void closeConnections() throws Exception {
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
        userDBManager.createUser(user);

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

        dataDBManager.createDataPoint(dataPoint1);
        dataDBManager.createDataPoint(dataPoint2);
        dataDBManager.createDataPoint(dataPoint3);
        dataDBManager.createDataPoint(dataPointOfAnotherType);

        final Statistic statistic = Statistics.getStatisticsForDataType(dataType);

        assertThat(statistic.getValue()).isEqualTo(average);
    }

    @Test
    public void testGetStatisticWithoutDataPointsHasValueZero() throws Exception {
        final DataPoint.DataType dataTypeWithoutDataPoints = DataPoint.DataType.TEMPERATURE;

        final Statistic statistic = Statistics.getStatisticsForDataType(dataTypeWithoutDataPoints);

        assertThat(statistic.getValue()).isEqualTo(0);
    }

    @Test
    public void testGetStatisticsForDemographicGroupOfUser() throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.HEART_RATE;
        final int value1 = 100;
        final int value2 = 200;
        final double average = (value1 + value2) / 2.0;

        final User user = User.builder()
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userDBManager.createUser(user);

        final User userWithinSameDemographicGroup = User.builder()
                .gender("gender")
                .birthDate(LocalDateTime.now().minusYears(2))
                .build();
        userDBManager.createUser(userWithinSameDemographicGroup);

        final User userInAnotherDemographicGroup = User.builder()
                .gender("anothergender")
                .birthDate(LocalDateTime.now().minusYears(10))
                .build();
        userDBManager.createUser(userInAnotherDemographicGroup);

        final DataPoint dataPoint1 = DataPoint.builder()
                .dataType(dataType)
                .userId(user.getId())
                .value(value1)
                .build();
        final DataPoint dataPoint2 = DataPoint.builder()
                .dataType(dataType)
                .userId(userWithinSameDemographicGroup.getId())
                .value(value2)
                .build();
        final DataPoint dataPointForAnotherUserInAnotherDemographicGroup = DataPoint.builder()
                .dataType(dataType)
                .userId(userInAnotherDemographicGroup.getId())
                .value(123)
                .build();

        dataDBManager.createDataPoint(dataPoint1);
        dataDBManager.createDataPoint(dataPoint2);
        dataDBManager.createDataPoint(dataPointForAnotherUserInAnotherDemographicGroup);

        final Statistic statistic = Statistics.getStatisticsInDemographicGroupOfUserForDataType(dataType, user);

        assertThat(statistic.getValue()).isEqualTo(average);
    }

    @Test
    public void testExcludingNonParticipatingUsers() throws Exception {
        final User participatingUser = User.builder()
                .participatingInAggregatedStatistics(true)
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        final User notParticipatingUser = User.builder()
                .participatingInAggregatedStatistics(false)
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();
        userDBManager.createUser(participatingUser);
        userDBManager.createUser(notParticipatingUser);

        final DataPoint.DataType dataType = DataPoint.DataType.HEART_RATE;
        final int value1 = 100;
        final int value2 = 200;
        final double average = (value1 + value2) / 2.0;

        final DataPoint dataPoint1 = DataPoint.builder()
                .dataType(dataType)
                .userId(participatingUser.getId())
                .value(value1)
                .build();
        final DataPoint dataPoint2 = DataPoint.builder()
                .dataType(dataType)
                .userId(participatingUser.getId())
                .value(value2)
                .build();
        final DataPoint excludedDataPoint = DataPoint.builder()
                .dataType(dataType)
                .userId(notParticipatingUser.getId())
                .value(123)
                .build();

        dataDBManager.createDataPoint(dataPoint1);
        dataDBManager.createDataPoint(dataPoint2);
        dataDBManager.createDataPoint(excludedDataPoint);

        final Statistic statistic = Statistics.getStatisticsForDataType(dataType);

        assertThat(statistic.getValue()).isEqualTo(average);

        final Statistic statisticInDemographicGroup =
                Statistics.getStatisticsInDemographicGroupOfUserForDataType(dataType, participatingUser);

        assertThat(statisticInDemographicGroup.getValue()).isEqualTo(average);
    }
}
