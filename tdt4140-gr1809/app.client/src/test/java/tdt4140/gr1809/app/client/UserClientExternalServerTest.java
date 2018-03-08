package tdt4140.gr1809.app.client;

import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserClientExternalServerTest {
    private static final UUID userId = UUID.fromString("d6183a12-e058-40cf-8850-18504f1ba270");
    private static final User user = User.builder()
            .id(userId)
            .firstName("Kari")
            .lastName("Nordmann")
            .gender("Female")
            .birthDate(LocalDate.of(1990, Month.MAY, 11).atStartOfDay())
            .build();

    private UserClient client = new UserClient();

    @Test
    public void testGetUser() {
        final Optional<User> retrievedUser = client.getUserById(userId);
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }

    @Test
    public void testCreateAndGetUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        client.createUser(user);

        final Optional<User> retrievedUser = client.getUserById(user.getId());

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
        client.createUser(user);

        final User newUser = User.from(user)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        client.updateUser(newUser);

        final Optional<User> updatedUser = client.getUserById(user.getId());

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
        client.createUser(user);

        client.deleteUser(user.getId());

        final Optional<User> retrievedUser = client.getUserById(user.getId());
        assertThat(retrievedUser).isEmpty();
    }

    @Test
    public void testGetInvalidUser() {
        final UUID invalidUserId = UUID.randomUUID();
        final Optional<User> retrievedUser = client.getUserById(invalidUserId);
        assertThat(retrievedUser).isEmpty();
    }

    @Test
    public void testUpdateInvalidUser() {
        final UUID invalidUserId = UUID.randomUUID();
        final User user = User.builder()
                .id(invalidUserId)
                .build();
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> client.updateUser(user));
    }
}
