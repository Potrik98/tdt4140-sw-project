package tdt4140.gr1809.app.client;

import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.UUID;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tdt4140.gr1809.app.core.model.ServiceProvider;

public class ServiceProviderClient extends BasicClient {

	public ServiceProviderClient() {
		super();
	}
	
    public Optional<ServiceProvider> getServiceProviderById(final UUID serviceProviderId) {
        final Response response = target
                .path("/serviceprovider/")
                .path(serviceProviderId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final ServiceProvider serviceProvider = response.readEntity(ServiceProvider.class);
            response.close();
            return Optional.of(serviceProvider);
        } else if (response.getStatus() == HttpURLConnection.HTTP_NOT_FOUND) {
            response.close();
            return Optional.empty();
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get service provider "
                .concat(serviceProviderId.toString()));
    }
    
    public void createServiceProvider(final ServiceProvider serviceProvider) {
        final Response response = target
                .path("/serviceprovider")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(serviceProvider));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            response.close();
            throw new ClientException("Failed to create service provider.");
        }
        response.close();
    }
    
    public void updateServiceProvider(final ServiceProvider serviceProvider) {
        final Response response = target
                .path("/serviceprovider/")
                .path(serviceProvider.getId().toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(serviceProvider));
        if (response.getStatus() != HttpURLConnection.HTTP_OK) {
            response.close();
            throw new ClientException("Failed to update service provider "
                    .concat(serviceProvider.getId().toString()));
        }
        response.close();
    }
    
    public void deleteServiceProvider(final UUID serviceProviderId) {
        final Response response = target
                .path("/serviceprovider/")
                .path(serviceProviderId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
            response.close();
            throw new ClientException("Failed to delete service provider "
                    .concat(serviceProviderId.toString()));
        }
        response.close();
    }
	
}
