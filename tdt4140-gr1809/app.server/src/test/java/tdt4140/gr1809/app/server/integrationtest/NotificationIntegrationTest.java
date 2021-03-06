package tdt4140.gr1809.app.server.integrationtest;

import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.userClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import tdt4140.gr1809.app.client.ClientException;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.notificationClient;

public class NotificationIntegrationTest {
	 @BeforeClass
	    public static void setupIntegrationTest() throws Exception {
	        IntegrationTestHelper.setupIntegrationTest();
	    }

	    @AfterClass
	    public static void closeConnections() throws Exception {
	        IntegrationTestHelper.stopIntegrationTest();
	    }
	    
	    @Test
	    public void testCreateAndGetNotifications() {
	    	final User user = User.builder()
	                .firstName("FirstName")
	                .lastName("LastName")
	                .birthDate(LocalDateTime.now())
	                .gender("gender")
					.maxPulse(123)
	                .build();
	        userClient.createUser(user);
	        
	        final Notification n1 = Notification.builder()
	        		.id(UUID.randomUUID())
	        		.userId(user.getId())
	        		.time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	        		.message("Hei og h�, du kommer til � d� igjen.")
	        		.build();
	        notificationClient.createNotification(n1);
	        
	        final Notification n2 = Notification.builder()
	        		.id(UUID.randomUUID())
	        		.userId(user.getId())
	        		.time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	        		.message("You fucked up now.")
	        		.build();
	        notificationClient.createNotification(n2);
	        
	        List<Notification> notifications = notificationClient.getNotificationsByUserId(user.getId());
	        assertThat(notifications).usingFieldByFieldElementComparator()
	        		.containsExactly(n1, n2);
	    }
	    
	    @Test
	    public void testCreateInvalidNotification() {
	    	final Notification n1 = Notification.builder()
	    			.userId(UUID.randomUUID())
	    			.message("Denne b�r ikke funke.")
	    			.time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	    			.build();
	    	assertThatExceptionOfType(ClientException.class)
	    			.isThrownBy(() -> notificationClient.createNotification(n1));
	    }
	    
	    @Test
	    public void testDeleteNotification() {
	    	final User user = User.builder()
	                .firstName("FirstName")
	                .lastName("LastName")
	                .birthDate(LocalDateTime.now())
	                .gender("gender")
					.maxPulse(123)
	                .build();
	        userClient.createUser(user);
	        
	        final Notification notification = Notification.builder()
	        		.id(UUID.randomUUID())
	        		.userId(user.getId())
	        		.time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	        		.message("Hei og h�, du kommer til � d� igjen.")
	        		.build();
	        notificationClient.createNotification(notification);
	        
	        notificationClient.deleteNotification(notification.getId());

	        final List<Notification> retrievedNotification = notificationClient.getNotificationsByUserId(user.getId());
	        assertThat(retrievedNotification).isEmpty();
	    }

}
