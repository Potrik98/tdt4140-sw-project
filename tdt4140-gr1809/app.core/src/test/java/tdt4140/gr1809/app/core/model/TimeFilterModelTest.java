package tdt4140.gr1809.app.core.model;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeFilterModelTest {
    @Test
    public void testParseTimeFilterModelToJson() throws IOException {
        final TimeFilter timeFilter = TimeFilter.builder()
                .userId(UUID.randomUUID())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();

        final String json = User.mapper.writeValueAsString(timeFilter);
        final TimeFilter parsedTimeFilter = User.mapper.readValue(json, TimeFilter.class);
        assertThat(parsedTimeFilter).isEqualToComparingFieldByField(timeFilter);
    }

    @Test
    public void testBuilder() {
        final TimeFilter timeFilter = TimeFilter.builder()
                .userId(UUID.randomUUID())
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .dataType(DataPoint.DataType.HEART_RATE)
                .build();
        final LocalDateTime someTime = LocalDateTime.now();
        final UUID someId = UUID.randomUUID();
        final TimeFilter newTimeFilter = TimeFilter.from(timeFilter)
                .startTime(someTime)
                .id(someId)
                .build();
        assertThat(newTimeFilter).isEqualToIgnoringGivenFields(timeFilter,
                "id", "startTime");
        assertThat(newTimeFilter.getId()).isEqualTo(someId);
        assertThat(newTimeFilter.getStartTime()).isEqualTo(someTime);
    }
}
