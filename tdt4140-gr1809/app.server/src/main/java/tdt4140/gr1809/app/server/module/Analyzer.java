package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableMap;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckCall;
import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckRun;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.notificationDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class Analyzer {
    private final Map<DataPoint.DataType, BiFunction<DataPoint, User, Optional<Notification>>>
            notificationGeneratorFunctionsByDataType = ImmutableMap.of(
                    DataPoint.DataType.HEART_RATE, this::getNotificationForHeartRateDataPoint,
                    DataPoint.DataType.STEPS, this::getNotificationForStepsDataPoint,
                    DataPoint.DataType.TEMPERATURE, this::getNotificationForTemperatureDataPoint
    );

    public void analyzeDataPoints(final List<DataPoint> dataPoints) {
        final List<Notification> notifications = getNotificationsOfDataPoints(dataPoints);
        notifications.forEach(notification -> uncheckRun(() ->
                notificationDBManager.createNotification(notification)));
    }

    private List<Notification> getNotificationsOfDataPoints(final List<DataPoint> dataPoints) {
        final Map<UUID, User> userMap = dataPoints.stream()
                .map(DataPoint::getUserId)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        userId -> uncheckCall(() -> userDBManager.getUserById(userId).get())));
        final Map<DataPoint.DataType, List<DataPoint>> dataPointsByType = dataPoints.stream()
                .collect(groupingBy(DataPoint::getDataType));

        return dataPointsByType.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream()
                        .map(dataPoint -> notificationGeneratorFunctionsByDataType.get(entry.getKey())
                                .apply(dataPoint, userMap.get(dataPoint.getUserId()))
                        ))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Notification> getNotificationForHeartRateDataPoint(final DataPoint dataPoint,
                                                                        final User user) {
        if (dataPoint.getDataType() != DataPoint.DataType.HEART_RATE)
            throw new IllegalArgumentException("Invalid datatype, expected HEART_RATE, but was "
                    + dataPoint.getDataType());
        if (dataPoint.getValue() > user.getMaxPulse()) {
            return Optional.of(Notification.builder()
                    .time(LocalDateTime.now())
                    .userId(user.getId())
                    .message("Pulse was over max-pulse at " + dataPoint.getTime()
                            + ".\nValue: " + dataPoint.getValue())
                    .build());
        }
        return Optional.empty();
    }

    private Optional<Notification> getNotificationForStepsDataPoint(final DataPoint dataPoint,
                                                                    final User user) {
        if (dataPoint.getDataType() != DataPoint.DataType.STEPS)
            throw new IllegalArgumentException("Invalid datatype, expected STEPS, but was "
                    + dataPoint.getDataType());
        return Optional.empty();
    }

    private Optional<Notification> getNotificationForTemperatureDataPoint(final DataPoint dataPoint,
                                                                          final User user) {
        if (dataPoint.getDataType() != DataPoint.DataType.TEMPERATURE)
            throw new IllegalArgumentException("Invalid datatype, expected TEMPERATURE, but was "
                    + dataPoint.getDataType());
        if (dataPoint.getValue() < 3750) {
            return Optional.of(Notification.builder()
                    .time(LocalDateTime.now())
                    .userId(user.getId())
                    .message("Low body temperature at " + dataPoint.getTime()
                            + ".\nValue: " + dataPoint.getValue())
                    .build());
        }
        if (dataPoint.getValue() > 3800) {
            return Optional.of(Notification.builder()
                    .time(LocalDateTime.now())
                    .userId(user.getId())
                    .message("High body temperature at " + dataPoint.getTime()
                            + ".\nValue: " + dataPoint.getValue())
                    .build());
        }
        return Optional.empty();
    }
}
