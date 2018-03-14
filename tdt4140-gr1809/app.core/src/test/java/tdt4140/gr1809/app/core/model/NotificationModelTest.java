package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.Notification;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationModelTest {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    public void testParseNotificationModelToJson() throws IOException {
        final Notification notification = Notification.builder()
                .userId(UUID.randomUUID())
                .time(LocalDateTime.now())
                .message("")
                .build();

        final String json = mapper.writeValueAsString(notification);
        final Notification parsedNotification = mapper.readValue(json, Notification.class);
        assertThat(parsedNotification).isEqualToComparingFieldByField(notification);
    }
}
