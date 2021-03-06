package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationDBManagerTest {

    private static NotificationDBManager notificationDBManager;
    private static UserDBManager userDBManager;

    @BeforeClass
    public static void openConnection() throws Exception {
    	userDBManager = new UserDBManager();
        notificationDBManager = new NotificationDBManager();
        DBManager.loadCreateScript();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
    	userDBManager.closeConnection();
        notificationDBManager.closeConnection();
    }
    
    
    @Test
    public void testCreateAndGetNotification() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .build();
        userDBManager.createUser(user);
        final Notification notification = Notification.builder()
        		.userId(user.getId())
                .message("Hei og h�, du kommer til � d� snart")
                .time(LocalDateTime.now())
                .build();

        notificationDBManager.createNotification(notification);

        final List<Notification> retrievedNotifications = notificationDBManager.getNotificationsByUserId(user.getId());

        assertThat(retrievedNotifications).usingFieldByFieldElementComparator()
                .containsExactly(notification);
    }
    
    @Test
    public void testGetNotificationInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final List<Notification> retrievedNotification = notificationDBManager.getNotificationsByUserId(invalidUserId);

        assertThat(retrievedNotification).isEmpty();
    }
    
    @Test
    public void testDeleteNotification() throws SQLException {
    	final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .build();
        userDBManager.createUser(user);
        final Notification notification = Notification.builder()
        		.userId(user.getId())
                .message("Hei og h�, du kommer til � d� snart")
                .time(LocalDateTime.now())
                .build();

        notificationDBManager.createNotification(notification);

        final List<Notification> retrievedNotifications = notificationDBManager.getNotificationsByUserId(user.getId());

        assertThat(retrievedNotifications).usingFieldByFieldElementComparator()
                .containsExactly(notification);
        notificationDBManager.deleteNotification(notification.getId());

        final List<Notification> deletedNotification = notificationDBManager.getNotificationsByUserId(user.getId());

        assertThat(deletedNotification).isEmpty();
    }
    
}