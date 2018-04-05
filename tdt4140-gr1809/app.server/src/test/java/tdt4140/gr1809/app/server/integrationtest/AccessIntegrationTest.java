package tdt4140.gr1809.app.server.integrationtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.integrationtest.IntegrationTestHelper.*;

public class AccessIntegrationTest {
    @BeforeClass
    public static void setupIntegrationTest() throws Exception {
        IntegrationTestHelper.setupIntegrationTest();
    }

    @AfterClass
    public static void closeConnections() throws Exception {
        IntegrationTestHelper.stopIntegrationTest();
    }

    @Test
    public void testGrantAndGetAccessToUsers() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        serviceProviderClient.createServiceProvider(serviceProvider);

        accessClient.giveServiceProviderAccessToUser(serviceProvider.getId(), user.getId());

        final List<ServiceProvider> serviceProviderWithAccessToUser =
                accessClient.getServiceProviderWithAccessToUser(user.getId());

        assertThat(serviceProviderWithAccessToUser).usingFieldByFieldElementComparator()
                .containsExactly(serviceProvider);

        final List<User> usersServiceProviderHasAccessTo =
                accessClient.getUsersServiceProviderHasAccessTo(serviceProvider.getId());

        assertThat(usersServiceProviderHasAccessTo).usingFieldByFieldElementComparator()
                .containsExactly(user);
    }

    @Test
    public void testRevokeAccess() {
        final User user = User.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .maxPulse(123)
                .build();
        userClient.createUser(user);

        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .gender("gender")
                .build();
        serviceProviderClient.createServiceProvider(serviceProvider);

        accessClient.giveServiceProviderAccessToUser(serviceProvider.getId(), user.getId());
        accessClient.revokeServiceProviderAccessToUser(serviceProvider.getId(), user.getId());

        final List<ServiceProvider> serviceProviderWithAccessToUser =
                accessClient.getServiceProviderWithAccessToUser(user.getId());
        final List<User> usersServiceProviderHasAccessTo =
                accessClient.getUsersServiceProviderHasAccessTo(serviceProvider.getId());

        assertThat(serviceProviderWithAccessToUser).isEmpty();
        assertThat(usersServiceProviderHasAccessTo).isEmpty();
    }
}
