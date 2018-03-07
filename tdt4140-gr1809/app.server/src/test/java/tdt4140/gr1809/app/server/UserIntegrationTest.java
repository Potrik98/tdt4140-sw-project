package tdt4140.gr1809.app.server;

import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1809.app.client.UserClient;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.UserDBManager;
import tdt4140.gr1809.app.server.resource.UserResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class UserIntegrationTest {
    public static final int TEST_PORT = 8192;
    private UserClient client;

    @Before
    public void setupIntegrationTest() throws Exception {
        Server.startServer(TEST_PORT);

        // Open connection to local h2 db in memory
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");

        // Read sql test create script
        InputStream input = UserDBManager.class.getResourceAsStream("sqlCreateScriptTest.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        // Execute sql test create script
        Statement statement = connection.createStatement();
        statement.execute(out.toString());

        // Init resources with db connection
        UserResource.init(connection);

        client = new UserClient("http://localhost:" + TEST_PORT);
    }

    @Test
    public void testCreateAndGetUser() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        client.createUser(user);

        final Optional<User> retrievedUser = client.getUserById(user.getId());

        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get()).isEqualToComparingFieldByField(user);
    }
}
