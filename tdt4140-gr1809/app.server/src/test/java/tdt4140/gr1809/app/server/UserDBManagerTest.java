package tdt4140.gr1809.app.server;

import org.junit.*;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDBManagerTest {
    private static UserDBManager dbManager;
    private static Connection connection;

    @BeforeClass
    public static void openConnection() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        InputStream input = UserDBManager.class.getResourceAsStream("sqlCreateScriptTest.sql");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        Statement statement = connection.createStatement();
        statement.execute(out.toString());

        System.out.println("Successfully loaded test db");

        dbManager = new UserDBManager(connection);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
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
