package tdt4140.gr1809.app.server.dbmanager;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessDBManagerTest {
	private static ServiceProviderDBManager serviceProviderDBManager;
    private static UserDBManager userDBManager;
    private static AccessTableDBManager accessDBManager;
    private static Connection connection;

    @BeforeClass
    public static void openConnection() throws Exception {
        serviceProviderDBManager = new ServiceProviderDBManager();
        userDBManager = new UserDBManager();
        accessDBManager = new AccessTableDBManager();
        DBManager.loadCreateScript();
    }
    
    @AfterClass
    public static void closeConnection() throws SQLException {
        serviceProviderDBManager.closeConnection();
        userDBManager.closeConnection();
        accessDBManager.closeConnection();
    }
    
    @Test
    public void testCreateAndGetRelation() throws SQLException {
    	final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    	userDBManager.createUser(user);
    	final ServiceProvider sp = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    	serviceProviderDBManager.createServiceProvider(sp);
    	
    	accessDBManager.giveServiceProviderAccessToUser(user.getId(), sp.getId());
    	
    	final List<User> spRelation = accessDBManager.getUsersServiceProviderHasAccessTo(sp.getId());
    	final List<ServiceProvider> userRelation = accessDBManager.getServiceProvidersWithAccessToUser(user.getId());
    	
    	
    	assertThat(spRelation).usingFieldByFieldElementComparator()
    			.containsExactly(user);
    	assertThat(userRelation).usingFieldByFieldElementComparator()
				.containsExactly(sp);	
    }
    
    @Test
    public void testrevokeServiceProviderAccessToUser() throws SQLException {
    	final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    	userDBManager.createUser(user);
    	final ServiceProvider sp = ServiceProvider.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .build();
    	serviceProviderDBManager.createServiceProvider(sp);
    	
    	accessDBManager.giveServiceProviderAccessToUser(user.getId(), sp.getId());
    	accessDBManager.revokeServiceProviderAccessToUser(user.getId(), sp.getId());
    	final List<User> spRelation = accessDBManager.getUsersServiceProviderHasAccessTo(sp.getId());
    	final List<ServiceProvider> userRelation = accessDBManager.getServiceProvidersWithAccessToUser(user.getId());
    	
    	assertThat(spRelation).isEmpty();
    	assertThat(userRelation).isEmpty();
    }
}
