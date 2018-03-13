package tdt4140.gr1809.app.core.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import tdt4140.gr1809.app.core.model.DataPoint;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DataPointModelTest {
    private static final ObjectMapper mapper = new ObjectMapper()
            .findAndRegisterModules();

    @Test
    public void testParseDataPointModelToJson() throws IOException {
        final DataPoint dataPoint = DataPoint.builder()
                .dataType(DataPoint.DataType.TEMPERATURE)
                .userId(UUID.randomUUID())
                .time(LocalDateTime.now())
                .value(123)
                .build();

        final String json = mapper.writeValueAsString(dataPoint);
        final DataPoint parsedDataPoint = mapper.readValue(json, DataPoint.class);
        assertThat(parsedDataPoint).isEqualToComparingFieldByField(dataPoint);
    }
}
