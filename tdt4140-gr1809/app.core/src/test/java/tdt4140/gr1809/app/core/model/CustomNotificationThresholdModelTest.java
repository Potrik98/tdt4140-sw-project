package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomNotificationThresholdModelTest {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    public void testParseCustomNotificationThresholdModelToJson() throws IOException {
        final CustomNotificationThreshold customNotificationThreshold = CustomNotificationThreshold.builder()
                .dataType(DataPoint.DataType.TEMPERATURE)
                .userId(UUID.randomUUID())
                .thresholdType(CustomNotificationThreshold.ThresholdType.LESS_THAN)
                .value(123)
                .build();

        final String json = mapper.writeValueAsString(customNotificationThreshold);
        final CustomNotificationThreshold parsedCustomNotificationThreshold = mapper.readValue(json, CustomNotificationThreshold.class);
        assertThat(parsedCustomNotificationThreshold).isEqualToComparingFieldByField(customNotificationThreshold);
    }
}
