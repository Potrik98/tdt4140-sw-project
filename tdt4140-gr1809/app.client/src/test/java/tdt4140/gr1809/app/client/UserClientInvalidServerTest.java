package tdt4140.gr1809.app.client;

import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserClientInvalidServerTest {
    private static final UserClient userClient = new UserClient("http://example.com");

    @Test
    public void testClient() {
        final UUID userId = UUID.randomUUID();
        final User user = User.builder()
                .id(userId)
                .build();
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> userClient.createUser(user));
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> userClient.updateUser(user));
        assertThatExceptionOfType(ClientException.class)
                .isThrownBy(() -> userClient.deleteUser(userId));
    }
}
