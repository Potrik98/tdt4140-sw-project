package tdt4140.gr1809.app.server.module;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.core.util.TimeUtils;

import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckCall;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.dataDBManager;
import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class Statistics {
    /**
     * Get overall aggregated statistic for all data points of participating users,
     * for a specific data type
     * @param dataType Data type to get statistic for
     * @return Statistic object containing the average
     */
    public static Statistic getStatisticsForDataType(final DataPoint.DataType dataType) throws Exception {
        final List<DataPoint> dataPointsOfDataType = dataDBManager.getDataPoints().stream()
                .filter(dataPoint -> dataPoint.getDataType() == dataType)
                .collect(Collectors.toList());
        // Create a map for userId to User object so that this isn't retrieved multiple times
        final Map<UUID, User> userIdToUserMap = dataPointsOfDataType.stream()
                .map(DataPoint::getUserId)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        userId -> uncheckCall(() -> userDBManager.getUserById(userId).get())));
        final List<DataPoint> dataPointsOfParticipatingUsers = dataPointsOfDataType.stream()
                .filter(dataPoint ->
                        userIdToUserMap.get(dataPoint.getUserId()).isParticipatingInAggregatedStatistics())
                .collect(Collectors.toList());
        if (dataPointsOfParticipatingUsers.size() == 0) {
            return Statistic.builder()
                    .value(0)
                    .build();
        }
        final int sum = dataPointsOfParticipatingUsers.stream()
                .mapToInt(DataPoint::getValue)
                .sum();
        final double average = ((double) sum) / dataPointsOfParticipatingUsers.size();
        return Statistic.builder()
                .value(average)
                .dataType(dataType)
                .build();
    }

    /**
     * Get statistic for the demographic group of the user, for the given data type.
     *
     * @param dataType Type of data for the statistic
     * @param user The user to find statistics for within the same demographic group
     * @return Statistic object containing the average
     */
    public static Statistic getStatisticsInDemographicGroupOfUserForDataType(final DataPoint.DataType dataType,
                                                                             final User user) throws Exception {
        final List<DataPoint> dataPointsOfDataType = dataDBManager.getDataPoints().stream()
                .filter(dataPoint -> dataPoint.getDataType() == dataType)
                .collect(Collectors.toList());
        // Create a map for userId to User object so that this isn't retrieved multiple times
        final Map<UUID, User> userIdToUserMap = dataPointsOfDataType.stream()
                .map(DataPoint::getUserId)
                .distinct()
                .collect(Collectors.toMap(
                        Function.identity(),
                        userId -> uncheckCall(() -> userDBManager.getUserById(userId).get())));

        final List<DataPoint> dataPointsOfDataTypeInDemographicGroupOfUser =
                dataPointsOfDataType.stream()
                .filter(dataPoint -> usersAreInSameDemographicGroup(user, userIdToUserMap.get(dataPoint.getUserId())))
                .collect(Collectors.toList());
        if (dataPointsOfDataTypeInDemographicGroupOfUser.size() == 0) {
            return Statistic.builder()
                    .value(0)
                    .build();
        }
        final List<DataPoint> dataPointsOfParticipatingUsers = dataPointsOfDataTypeInDemographicGroupOfUser.stream()
                .filter(dataPoint ->
                        userIdToUserMap.get(dataPoint.getUserId()).isParticipatingInAggregatedStatistics())
                .collect(Collectors.toList());
        final int sum = dataPointsOfParticipatingUsers.stream()
                .mapToInt(DataPoint::getValue)
                .sum();
        final double average = ((double) sum) / dataPointsOfParticipatingUsers.size();
        return Statistic.builder()
                .value(average)
                .dataType(dataType)
                .build();
    }

    /**
     * Checks if two users are in the same demographic group.
     * This is defined to be born within 5 years of each other,
     * and to have the same gender.
     */
    private static boolean usersAreInSameDemographicGroup(final User user1,
                                                          final User user2) {
        return user1.getGender().equals(user2.getGender()) &&
                TimeUtils.localDateTimesAreWithinAPeriodOfEachOther(
                        user1.getBirthDate(), user2.getBirthDate(), Period.ofYears(5));
    }
}
