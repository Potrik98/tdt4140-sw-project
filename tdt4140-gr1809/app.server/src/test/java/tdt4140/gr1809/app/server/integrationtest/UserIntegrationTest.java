package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.dataClient;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.notificationClient;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.userClient;

public class UserIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testCreateAndGetUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        final Optional<User> retrievedUser = userClient.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }
    
    @Test
    public void testCreateAndGetAllUserData() throws JsonProcessingException {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);
        
        final Notification n1 = Notification.builder()
        		.id(UUID.randomUUID())
        		.userId(user.getId())
        		.time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        		.message("Hei.")
        		.build();
        notificationClient.createNotification(n1);
        
        final DataPoint p1 = DataPoint.builder()
                .userId(user.getId())
                .dataType(DataPoint.DataType.STEPS)
                .time(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .value(100)
                .build();
        dataClient.createDataPoint(p1);
        
        final User userWithData = User.from(user)
                .notifications(ImmutableList.of(n1))
                .dataPoints(ImmutableList.of(p1))
                .build();

        final Optional<User> retrievedUser = userClient.getAllUserDataById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByFieldRecursively(userWithData);
    }

    @Test
    public void testUpdateUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        final User newUser = User.from(user)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .maxPulse(123)
                .build();
        userClient.updateUser(newUser);

        final Optional<User> updatedUser = userClient.getUserById(user.getId());

        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get()).isEqualToComparingFieldByField(newUser);
    }

    @Test
    public void testDeleteUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        userClient.deleteUser(user.getId());

        final Optional<User> retrievedUser = userClient.getUserById(user.getId());
        assertThat(retrievedUser).isEmpty();
    }
}
