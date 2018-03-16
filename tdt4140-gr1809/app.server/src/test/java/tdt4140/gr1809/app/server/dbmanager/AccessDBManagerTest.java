package tdt4140.gr1809.app.server.dbmanager;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;

public class AccessDBManagerTest {
	private static ServiceProviderDBManager serviceProviderDBManager;
    private static UserDBManager userDBManager;
    private static AccessTableDBManager accessDBManager;
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

        serviceProviderDBManager = new ServiceProviderDBManager(connection);
        userDBManager = new UserDBManager(connection);
        accessDBManager = new AccessTableDBManager(connection);
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
