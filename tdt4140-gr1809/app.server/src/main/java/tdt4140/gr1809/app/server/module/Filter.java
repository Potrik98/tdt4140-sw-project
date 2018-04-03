package tdt4140.gr1809.app.server.module;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.TimeFilter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.timeFilterDBManager;

public class Filter {
    public List<DataPoint> filterDataPoints(final List<DataPoint> unfilteredList) throws Exception {
        final Map<UUID, List<TimeFilter>> timeFiltersForUsers = unfilteredList.stream()
                .map(DataPoint::getUserId)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), this::getTimeFiltersForUserId));
        return unfilteredList.stream()
                .filter(dataPoint -> timeFiltersForUsers.get(dataPoint.getUserId()).stream()
                        .noneMatch(filter -> isDataPointInFilter(dataPoint, filter)))
                .collect(Collectors.toList());
    }

    private boolean isDataPointInFilter(final DataPoint dataPoint, final TimeFilter filter) {
        return dataPoint.getDataType() == filter.getDataType()
                && dataPoint.getTime().isAfter(filter.getStartTime())
                && dataPoint.getTime().isBefore(filter.getEndTime());
    }


    private List<TimeFilter> getTimeFiltersForUserId(final UUID userId) {
        try {
            return timeFilterDBManager.getTimeFiltersByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
