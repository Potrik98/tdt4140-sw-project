package tdt4140.gr1809.app.client;

import tdt4140.gr1809.app.core.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Optional;
import java.util.UUID;

public class UserClient extends BasicClient {

    public UserClient() {
        super();
    }

    public Optional<User> getUserById(final UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final User user = response.readEntity(User.class);
            response.close();
            return Optional.of(user);
        } else if (response.getStatus() == HttpURLConnection.HTTP_NOT_FOUND) {
            response.close();
            return Optional.empty();
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get user "
                .concat(userId.toString()));
    }
    
    public Optional<User> getAllUserDataById(final UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .path("/data")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final User user = response.readEntity(User.class);
            response.close();
            return Optional.of(user);
        } else if (response.getStatus() == HttpURLConnection.HTTP_NOT_FOUND) {
            response.close();
            return Optional.empty();
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get user data "
                .concat(userId.toString()));
    }
    
    

    public void createUser(final User user) {
        final Response response = target
                .path("/user")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            response.close();
            throw new ClientException("Failed to create user.");
        }
        response.close();
    }

    public void updateUser(final User user) {
        final Response response = target
                .path("/user/")
                .path(user.getId().toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user));
        if (response.getStatus() != HttpURLConnection.HTTP_OK) {
            response.close();
            throw new ClientException("Failed to update user "
                    .concat(user.getId().toString()));
        }
        response.close();
    }

    public void deleteUser(final UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
            response.close();
            throw new ClientException("Failed to delete user "
                    .concat(userId.toString()));
        }
        response.close();
    }
}
