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
        super("/users");
    }

    public Optional<User> getUserById(final UUID userId) {
        final Response response = target
                .path(userId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            return Optional.of(response.readEntity(User.class));
        } else if (response.getStatus() == HttpURLConnection.HTTP_NOT_FOUND) {
            return Optional.empty();
        }
        throw new ClientException("Failed to get user "
                .concat(userId.toString()).concat("\n")
                .concat(response.getEntity().toString()));
    }

    public void createUser(final User user) {
        final Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            throw new ClientException("Failed to create user:\n"
                    .concat(response.getEntity().toString()));
        }
    }

    public void updateUser(final User user) {
        final Response response = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(user));
        if (response.getStatus() != HttpURLConnection.HTTP_OK) {
            throw new ClientException("Failed to update user:\n"
                    .concat(response.getEntity().toString()));
        }
    }

    public void deleteUser(final UUID userId) {
        final Response response = target
                .path(userId.toString())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
        if (response.getStatus() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new ClientException("Failed to delete user:\n"
                    .concat(response.getEntity().toString()));
        }
    }
}
