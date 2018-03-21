package tdt4140.gr1809.app.datagen;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.IntegrationTestHelper;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.dataClient;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.userClient;

public class DataGeneratorTest {
    @BeforeClass
    public static void setupTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void stopConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testDataGeneration() throws Exception {
        final User user = User.builder()
                .birthDate(LocalDateTime.now())
                .build();
        userClient.createUser(user);

        final int count = 5;
        final DataPoint.DataType dataType = DataPoint.DataType.TEMPERATURE;

        final String[] args = {
                dataType.name(),
                user.getId().toString(),
                String.valueOf(count)
        };

        DataGenerator.main(args);

        final List<DataPoint> dataPoints = dataClient.getDataPointsForUserId(user.getId());
        assertThat(dataPoints).hasSize(count);
        assertThat(dataPoints).extracting("dataType").containsOnly(dataType);
    }
}
