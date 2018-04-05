package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomNotificationThresholdDBManagerTest {
    private static CustomNotificationThresholdDBManager customNotificationThresholdDBManager;
    private static UserDBManager userDBManager;

    @BeforeClass
    public static void openConnection() throws Exception {
        customNotificationThresholdDBManager = new CustomNotificationThresholdDBManager();
        userDBManager = new UserDBManager();
        DBManager.loadCreateScript();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        customNotificationThresholdDBManager.closeConnection();
        userDBManager.closeConnection();
    }

    @Test
    public void testCreateAndGetCustomNotificationThreshold() throws SQLException {
        final User user = User.builder().build();
        final User anotherUser = User.builder().build();
        userDBManager.createUser(user);
        userDBManager.createUser(anotherUser);

        final CustomNotificationThreshold customNotificationThreshold1 = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(40)
                .build();
        final CustomNotificationThreshold customNotificationThreshold2 = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.STEPS)
                .thresholdType(CustomNotificationThreshold.ThresholdType.MORE_THAN)
                .value(20)
                .build();
        final CustomNotificationThreshold customNotificationThresholdForAnotherUser =
                CustomNotificationThreshold.builder()
                .userId(anotherUser.getId())
                .dataType(DataPoint.DataType.TEMPERATURE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(30)
                .build();

        customNotificationThresholdDBManager.createCustomNotificationThreshold(customNotificationThreshold1);
        customNotificationThresholdDBManager.createCustomNotificationThreshold(customNotificationThreshold2);
        customNotificationThresholdDBManager
                .createCustomNotificationThreshold(customNotificationThresholdForAnotherUser);

        final List<CustomNotificationThreshold> retrievedCustomNotificationThresholds =
                customNotificationThresholdDBManager.getCustomNotificationThresholdsByUserId(user.getId());

        assertThat(retrievedCustomNotificationThresholds).usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(customNotificationThreshold1, customNotificationThreshold2);
    }

    @Test
    public void testUpdateCustomNotificationThreshold() throws SQLException {
        final User user = User.builder().build();
        userDBManager.createUser(user);

        final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(40)
                .build();
        customNotificationThresholdDBManager.createCustomNotificationThreshold(customNotificationThreshold);

        final CustomNotificationThreshold updatedCustomNotificationThreshold = CustomNotificationThreshold
                .from(customNotificationThreshold)
                .thresholdType(CustomNotificationThreshold.ThresholdType.MORE_THAN)
                .value(123)
                .build();
        customNotificationThresholdDBManager.updateCustomNotificationThreshold(updatedCustomNotificationThreshold);

        final List<CustomNotificationThreshold> retrievedCustomNotificationThresholds =
                customNotificationThresholdDBManager.getCustomNotificationThresholdsByUserId(user.getId());

        assertThat(retrievedCustomNotificationThresholds).usingFieldByFieldElementComparator()
                .containsExactly(updatedCustomNotificationThreshold);
    }

    @Test
    public void testDeleteCustomNotificationThreshold() throws SQLException {
        final User user = User.builder().build();
        userDBManager.createUser(user);

        final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.HEART_RATE)
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(40)
                .build();
        customNotificationThresholdDBManager.createCustomNotificationThreshold(customNotificationThreshold);

        customNotificationThresholdDBManager.deleteCustomNotificationThreshold(customNotificationThreshold.getId());

        final List<CustomNotificationThreshold> customNotificationThresholdsForUser =
                customNotificationThresholdDBManager.getCustomNotificationThresholdsByUserId(user.getId());

        assertThat(customNotificationThresholdsForUser).isEmpty();
    }
}
