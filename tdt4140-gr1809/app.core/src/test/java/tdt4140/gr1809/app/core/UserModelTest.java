package tdt4140.gr1809.app.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class UserModelTest {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    public void testParseUserModelToJson() throws IOException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .build();

        final String json = mapper.writeValueAsString(user);
        final User parsedUser = mapper.readValue(json, User.class);
        assertThat(parsedUser).isEqualToComparingFieldByField(user);
    }
}
