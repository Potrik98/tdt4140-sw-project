package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDBManagerTest {
    private static UserDBManager dbManager;

    @BeforeClass
    public static void openConnection() throws Exception {
        dbManager = new UserDBManager();
        DBManager.loadCreateScript();
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        dbManager.closeConnection();
    }

    @Test
    public void testCreateAndGetUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createUser(user);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }

    @Test
    public void testGetUserInvalidId() throws SQLException {
        final UUID invalidUserId = UUID.randomUUID();

        final Optional<User> retrievedUser = dbManager.getUserById(invalidUserId);

        assertThat(retrievedUser).isEmpty();
    }

    @Test
    public void testUpdateUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createUser(user);

        final User updatedUser = User.from(user)
                .firstName("Newfirstname")
                .lastName("Newlastname")
                .gender("Gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.updateUser(updatedUser);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(updatedUser);
    }

    @Test
    public void testDeleteUser() throws SQLException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createUser(user);

        final Optional<User> retrievedUser = dbManager.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);

        dbManager.deleteUser(user.getId());

        final Optional<User> deletedUser = dbManager.getUserById(user.getId());

        assertThat(deletedUser).isEmpty();
    }
}
