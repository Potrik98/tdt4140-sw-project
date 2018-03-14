package tdt4140.gr1809.app.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;

public class ServiceProviderModelTest {
	@Test
    public void testParseServiceProviderModelToJson() throws IOException {
        final ServiceProvider serviceProvider = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        final String json = ServiceProvider.mapper.writeValueAsString(serviceProvider);
        final ServiceProvider parsedServiceProvider = ServiceProvider.mapper.readValue(json, ServiceProvider.class);
        assertThat(parsedServiceProvider).isEqualToComparingFieldByField(serviceProvider);
    }

}
