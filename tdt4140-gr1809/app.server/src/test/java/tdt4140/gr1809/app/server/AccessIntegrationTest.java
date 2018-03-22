package tdt4140.gr1809.app.server;

import static tdt4140.gr1809.app.server.IntegrationTestHelper.serviceProviderClient;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.userClient;
import static org.assertj.core.api.Assertions.assertThat;
import static tdt4140.gr1809.app.server.IntegrationTestHelper.accessClient;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;

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
	                .birthDate(LocalDateTime.now())
	                .gender("gender")
	                .build();
	        userClient.createUser(user);
	        
	        final ServiceProvider serviceProvider = ServiceProvider.builder()
	                .firstName("FirstName")
	                .lastName("LastName")
	                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
	                .gender("gender")
	                .build();
	        serviceProviderClient.createServiceProvider(serviceProvider);
	        
	        accessClient.giveServiceProviderAccessToUser(user.getId(), serviceProvider.getId());
	        
	    	final List<ServiceProvider> userRelation = accessClient.getServiceProviderWithAccessToUser(user.getId());
	    	
	    	assertThat(userRelation).usingFieldByFieldElementComparator()
					.containsExactly(serviceProvider);
	    }
	    
	    
	    

}
