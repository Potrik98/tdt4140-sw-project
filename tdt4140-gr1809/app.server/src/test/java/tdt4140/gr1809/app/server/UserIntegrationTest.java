package tdt4140.gr1809.app.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.userClient;

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
                .build();
        userClient.createUser(user);

        final Optional<User> retrievedUser = userClient.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }

    @Test
    public void testUpdateUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        userClient.createUser(user);

        final User newUser = User.from(user)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
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
                .build();
        userClient.createUser(user);

        userClient.deleteUser(user.getId());

        final Optional<User> retrievedUser = userClient.getUserById(user.getId());
        assertThat(retrievedUser).isEmpty();
    }
}
