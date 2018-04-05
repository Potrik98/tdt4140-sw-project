package tdt4140.gr1809.app.server.module;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import tdt4140.gr1809.app.core.model.CustomNotificationThreshold;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Notification;
import tdt4140.gr1809.app.core.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckCall;
import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckRun;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.customNotificationThresholdDBManager;
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

    /**
     * Generates a list of notifications for the given list of data points.
     * The method will look up any custom notification thresholds for the
     * corresponding users for the data points, and also analyze them
     * using the default analyzation methods for the different data types.
     * @param dataPoints Data points to analyze
     * @return List of notifications
     */
    private List<Notification> getNotificationsOfDataPoints(final List<DataPoint> dataPoints) {
        // Create a map of userId to user so that this isn't queried for every data point
        final Map<UUID, User> userMap = dataPoints.stream()
                .map(DataPoint::getUserId)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        userId -> uncheckCall(() -> userDBManager.getUserById(userId).get())));

        // Create a map of userId to a list of custom notification thresholds per data type,
        // so that it is possible to easily look up the custom notification threshold a user
        // has registered for a specific data type.
        final Map<UUID, Map<DataPoint.DataType, List<CustomNotificationThreshold>>>
                customNotificationThresholdsForUserByDataType =
                userMap.keySet().stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                userId -> uncheckCall(() ->
                                        customNotificationThresholdDBManager
                                                .getCustomNotificationThresholdsByUserId(userId)).stream()
                                .collect(Collectors.groupingBy(CustomNotificationThreshold::getDataType))));
        // Group the data points by their data type
        final Map<DataPoint.DataType, List<DataPoint>> dataPointsByType = dataPoints.stream()
                .collect(groupingBy(DataPoint::getDataType));

        // Return a list of notifications containing both standard generated notifications,
        // and custom generated notifications.
        return ImmutableList.<Notification>builder()
                // Add standard generated notifications
                .addAll(dataPointsByType.entrySet().stream()
                        .flatMap(entry -> entry.getValue().stream()
                                .map(dataPoint -> notificationGeneratorFunctionsByDataType.get(entry.getKey())
                                        .apply(dataPoint, userMap.get(dataPoint.getUserId()))
                                        // Use the generator function for the right data type,
                                        // and analyze the data point
                                ))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList()))
                // Add custom generated notifications
                .addAll(dataPointsByType.entrySet().stream()
                        .flatMap(entry -> entry.getValue().stream()
                                .map(dataPoint -> getNotificationsForCustomThresholds(
                                        dataPoint,
                                        customNotificationThresholdsForUserByDataType
                                                .get(dataPoint.getUserId())
                                                .get(dataPoint.getDataType()))))
                        .flatMap(List::stream)
                        .collect(Collectors.toList()))
                .build();
    }

    private List<Notification> getNotificationsForCustomThresholds(
            final DataPoint dataPoint,
            final List<CustomNotificationThreshold> customNotificationThresholds) {
        if (Objects.isNull(customNotificationThresholds))
            return ImmutableList.of();
        return customNotificationThresholds.stream()
                .filter(customNotificationThreshold ->
                        customNotificationThreshold.getThresholdType()
                                == CustomNotificationThreshold.ThresholdType.LESS_THAN &&
                        customNotificationThreshold.getValue() > dataPoint.getValue()
                                || customNotificationThreshold.getThresholdType()
                                == CustomNotificationThreshold.ThresholdType.MORE_THAN &&
                                customNotificationThreshold.getValue() < dataPoint.getValue())
                .map(customNotificationThreshold -> Notification.builder()
                        .time(LocalDateTime.now())
                        .userId(dataPoint.getUserId())
                        .message(customNotificationThreshold.getMessage()
                                .concat("\nTime: " + dataPoint.getTime())
                                .concat("\nValue: " + dataPoint.getValue()))
                        .build())
                .collect(Collectors.toList());
    }

    private Optional<Notification> getNotificationForHeartRateDataPoint(final DataPoint dataPoint,
                                                                        final User user) {
        if (dataPoint.getDataType() != DataPoint.DataType.HEART_RATE)
            throw new IllegalArgumentException("Invalid datatype, expected HEART_RATE, but was "
                    + dataPoint.getDataType());
        if (!Objects.isNull(user.getMaxPulse()) && dataPoint.getValue() > user.getMaxPulse()) {
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
