package tdt4140.gr1809.app.server.dbmanager;

import tdt4140.gr1809.app.core.model.User;

import java.util.Optional;
import java.util.UUID;

public class UserDBManager extends DBManager {
    public UserDBManager() {
        super();
    }

    public Optional<User> getUserById(final UUID userId) {
        return Optional.empty();
    }

    public void putUser(final User user) {

    }

    public void deleteUser(final UUID userId) {
        // Soft delete user
    }
}
