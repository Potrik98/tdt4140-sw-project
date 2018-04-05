package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.ServiceProvider;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.serviceProviderClient;

public class ServiceProviderIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }
    
    @Test
    public void testCreateAndGetServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        serviceProviderClient.createServiceProvider(serviceProvider);

        final Optional<ServiceProvider> retrievedServiceProvider =
                serviceProviderClient.getServiceProviderById(serviceProvider.getId());

        assertThat(retrievedServiceProvider).isPresent();
        assertThat(retrievedServiceProvider.get()).isEqualToComparingFieldByField(serviceProvider);
    }

    @Test
    public void testUpdateServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        serviceProviderClient.createServiceProvider(serviceProvider);

        final ServiceProvider newServiceProvider = ServiceProvider.from(serviceProvider)
                .firstName("NewFirstName")
                .lastName("NewLastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
        serviceProviderClient.updateServiceProvider(newServiceProvider);

        final Optional<ServiceProvider> updatedServiceProvider =
                serviceProviderClient.getServiceProviderById(serviceProvider.getId());

        assertThat(updatedServiceProvider).isPresent();
        assertThat(updatedServiceProvider.get()).isEqualToComparingFieldByField(newServiceProvider);
    }

    @Test
    public void testDeleteServiceProvider() {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        serviceProviderClient.createServiceProvider(serviceProvider);

        serviceProviderClient.deleteServiceProvider(serviceProvider.getId());

        final Optional<ServiceProvider> retrievedServiceProvider =
                serviceProviderClient.getServiceProviderById(serviceProvider.getId());
        assertThat(retrievedServiceProvider).isEmpty();
    }
}
