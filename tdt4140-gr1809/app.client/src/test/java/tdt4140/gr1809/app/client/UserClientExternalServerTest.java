package tdt4140.gr1809.app.client;

import org.junit.Ignore;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserClientExternalServerTest {
    private static final UUID userId = UUID.fromString("d6183a12-e058-40cf-8850-18504f1ba270");
    private static final User user = User.builder()
            .id(userId)
            .firstName("Kari")
            .lastName("Nordmann")
            .gender("Female")
            .birthDate(LocalDate.of(1990, Month.MAY, 11).atStartOfDay())
            .build();

    @Test
    public void testGetUser() {
        final UserClient userClient = new UserClient();
        final Optional<User> retrievedUser = userClient.getUserById(userId);
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }
}
