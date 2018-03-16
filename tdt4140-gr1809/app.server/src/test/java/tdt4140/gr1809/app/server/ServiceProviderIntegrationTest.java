package tdt4140.gr1809.app.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.client.ServiceProviderClient;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.server.resource.ServiceProviderResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceProviderIntegrationTest {
    public static final int TEST_PORT = 8192;
    private static ServiceProviderClient client;
    private static Connection connection;

    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        Server.startServer(TEST_PORT);

        // Open connection to local h2 db in memory
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        // Read sql test create script
        InputStream input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");
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
        ServiceProviderResource.init(connection);

        client = new ServiceProviderClient("http://localhost:" + TEST_PORT);
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        connection.close();
        Server.stopServer();
        // Remove when https://github.com/perwendel/spark/issues/705 is fixed.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateAndGetServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        client.createServiceProvider(serviceProvider);

        final Optional<ServiceProvider> retrievedServiceProvider = client.getServiceProviderById(serviceProvider.getId());

        assertThat(retrievedServiceProvider).isPresent();
        assertThat(retrievedServiceProvider.get()).isEqualToComparingFieldByField(serviceProvider);
    }

    @Test
    public void testUpdateServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        client.createServiceProvider(serviceProvider);

        final ServiceProvider newServiceProvider = ServiceProvider.from(serviceProvider)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthDate(LocalDateTime.now())
                .build();
        client.updateServiceProvider(newServiceProvider);

        final Optional<ServiceProvider> updatedServiceProvider = client.getServiceProviderById(serviceProvider.getId());

        assertThat(updatedServiceProvider).isPresent();
        assertThat(updatedServiceProvider.get()).isEqualToComparingFieldByField(newServiceProvider);
    }

    @Test
    public void testDeleteServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now())
                .gender("gender")
                .build();
        client.createServiceProvider(serviceProvider);

        client.deleteServiceProvider(serviceProvider.getId());

        final Optional<ServiceProvider> retrievedServiceProvider = client.getServiceProviderById(serviceProvider.getId());
        assertThat(retrievedServiceProvider).isEmpty();
    }
}

