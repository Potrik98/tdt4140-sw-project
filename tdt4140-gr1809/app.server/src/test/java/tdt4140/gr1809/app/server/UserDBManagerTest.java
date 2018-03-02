package tdt4140.gr1809.app.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;

public class UserDBManagerTest extends DBManagerTest {
    private UserDBManager dbManager;

    public UserDBManagerTest() throws SQLException {
        super();
        dbManager = new UserDBManager();
    }

    @Test
    @Ignore
    public void testCreateAndGetUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.putUser(user);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(user);
    }

    @Test
    @Ignore
    public void testGetUserInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final Optional<User> retrievedUser = dbManager.getUserById(invalidUserId);

        assertThat(retrievedUser).isEmpty();
    }

    @Test
    @Ignore
    public void testUpdateUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.putUser(user);

        final User updatedUser = User.from(user)
                .firstName("Newfirstname")
                .lastName("Newlastname")
                .gender("Gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.putUser(user);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(updatedUser);
    }

    @Test
    @Ignore
    public void testDeleteUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.putUser(user);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualTo(user);

        dbManager.deleteUser(user.getId());

        final Optional<User> deletedUser = dbManager.getUserById(user.getId());

        assertThat(deletedUser).isEmpty();
    }
}
