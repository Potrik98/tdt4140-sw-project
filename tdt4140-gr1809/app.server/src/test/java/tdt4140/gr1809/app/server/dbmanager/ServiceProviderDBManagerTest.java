package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.ServiceProvider;

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

public class ServiceProviderDBManagerTest {
    private static ServiceProviderDBManager dbManager;
    private static Connection connection;

    @BeforeClass
    public static void openConnection() throws Exception {
        Class.forName("org.h2.Driver");
        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        InputStream input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        Statement statement = connection.createStatement();
        statement.execute(out.toString());

        System.out.println("Successfully loaded test db");

        dbManager = new ServiceProviderDBManager(connection);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void testCreateAndGetServiceProvider() throws SQLException {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createServiceProvider(serviceProvider);

        final Optional<ServiceProvider> retrievedServiceProvider = dbManager.getServiceProviderById(serviceProvider.getId());

        assertThat(retrievedServiceProvider).isPresent();
        assertThat(retrievedServiceProvider.get()).isEqualToComparingFieldByField(serviceProvider);
    }

    @Test
    public void testGetServiceProviderInvalidId() throws SQLException {
        final UUID invalidServiceProviderId = UUID.randomUUID();

        final Optional<ServiceProvider> retrievedServiceProvider = dbManager.getServiceProviderById(invalidServiceProviderId);

        assertThat(retrievedServiceProvider).isEmpty();
    }

    @Test
    public void testUpdateServiceProvider() throws SQLException {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createServiceProvider(serviceProvider);

        final ServiceProvider updatedServiceProvider = ServiceProvider.from(serviceProvider)
                .firstName("Newfirstname")
                .lastName("Newlastname")
                .gender("Gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.updateServiceProvider(updatedServiceProvider);

        final Optional<ServiceProvider> retrievedServiceProvider = dbManager.getServiceProviderById(serviceProvider.getId());

        assertThat(retrievedServiceProvider).isPresent();
        assertThat(retrievedServiceProvider.get()).isEqualToComparingFieldByField(updatedServiceProvider);
    }

    @Test
    public void testDeleteServiceProvider() throws SQLException {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        dbManager.createServiceProvider(serviceProvider);

        final Optional<ServiceProvider> retrievedServiceProvider = dbManager.getServiceProviderById(serviceProvider.getId());

        assertThat(retrievedServiceProvider).isPresent();
        assertThat(retrievedServiceProvider.get()).isEqualToComparingFieldByField(serviceProvider);

        dbManager.deleteServiceProvider(serviceProvider.getId());

        final Optional<ServiceProvider> deletedServiceProvider = dbManager.getServiceProviderById(serviceProvider.getId());

        assertThat(deletedServiceProvider).isEmpty();
    }
}
