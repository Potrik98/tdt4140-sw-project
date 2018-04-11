package tdt4140.gr1809.app.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.LocalDateTime;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class UserModelTest {
    @Test
    public void testParseUserModelToJson() throws IOException {
        final User user = User.builder()
                .firstName("Firstname")
                .lastName("Lastname")
                .gender("gender")
                .birthDate(LocalDateTime.now())
                .maxPulse(123)
                .participatingInAggregatedStatistics(true)
                .notifications(ImmutableList.of(Notification.builder().build()))
                .dataPoints(ImmutableList.of(DataPoint.builder().build()))
                .build();

        final String json = User.mapper.writeValueAsString(user);
        final User parsedUser = User.mapper.readValue(json, User.class);
        assertThat(parsedUser).isEqualToComparingFieldByFieldRecursively(user);
    }
}
