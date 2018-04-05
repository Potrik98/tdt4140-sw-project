package tdt4140.gr1809.app.core.model;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticModelTest {
    @Test
    public void testParseStatisticModelToJson() throws IOException {
        final Statistic statistic = Statistic.builder()
                .dataType(DataPoint.DataType.HEART_RATE)
                .value(123)
                .build();

        final String json = Statistic.mapper.writeValueAsString(statistic);
        final Statistic parsedStatistic = Statistic.mapper.readValue(json, Statistic.class);
        assertThat(parsedStatistic).isEqualToComparingFieldByField(statistic);
    }
}
