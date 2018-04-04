package tdt4140.gr1809.app.server.module;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;

import java.util.List;
import java.util.stream.Collectors;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.dataDBManager;

public class Statistics {
    public static Statistic getStatisticsForDataType(final DataPoint.DataType dataType) throws Exception {
        final List<DataPoint> dataPointsOfDataType = dataDBManager.getDataPoints().stream()
                .filter(dataPoint -> dataPoint.getDataType() == dataType)
                .collect(Collectors.toList());
        final int sum = dataPointsOfDataType.stream()
                .mapToInt(DataPoint::getValue)
                .sum();
        final double average = ((double) sum) / dataPointsOfDataType.size();
        return Statistic.builder()
                .value(average)
                .build();
    }
}
