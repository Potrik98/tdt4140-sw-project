package tdt4140.gr1809.app.client;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tdt4140.gr1809.app.core.model.ServiceProvider;
import tdt4140.gr1809.app.core.model.User;

public class AccessClient extends BasicClient {
	public AccessClient() {
		super();
	}
	
	public List<ServiceProvider> getServiceProviderWithAccessToUser(UUID userId) {
		final Response response = target
			.path("/user/")
			.path(userId.toString())
			.path("/serviceproviders")
			.request(MediaType.APPLICATION_JSON_TYPE)
			.get();
		if (response.getStatus() == HttpURLConnection.HTTP_OK) {
			final List<ServiceProvider> serviceProviders = 
					response.readEntity(new GenericType<List<ServiceProvider>>() {});
			response.close();
			return serviceProviders;
		}
		System.out.println("Response: " + response.getStatus());
		response.close();
		throw new ClientException("Failed to get Service Providers with access to user "
				.concat(userId.toString()));
	}
	
	public void giveServiceProviderAccessToUser(final UUID serviceProviderId,
												final UUID userId) {
		final Response response = target
				.path("/user/")
				.path(userId.toString())
				.path("/serviceproviders/")
				.path(serviceProviderId.toString())
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.json(""));
		if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
			System.out.println("Response: " + response.getStatus());
			response.close();
			throw new ClientException("Failed to grant access to Service Provider");
		}
		response.close();
	}
	
	public void revokeServiceProviderAccessToUser(final UUID serviceProviderId,
												  final UUID userId) {
		final Response response = target
				.path("/user/")
				.path(userId.toString())
				.path("/serviceProviders")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.delete();
		if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
			System.out.println("Response: " + response.getStatus());
			response.close();
			throw new ClientException("Failed to revoke access to Service Provider");
		}
		response.close();
	}

	public List<User> getUsersServiceProviderHasAccessTo(final UUID serviceProviderId) {
        final Response response = target
                .path("/serviceprovider/")
                .path(serviceProviderId.toString())
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final List<User> users =
                    response.readEntity(new GenericType<List<User>>() {});
            response.close();
            return users;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get users ServiceProvider has access to "
                .concat(serviceProviderId.toString()));
    }
}
