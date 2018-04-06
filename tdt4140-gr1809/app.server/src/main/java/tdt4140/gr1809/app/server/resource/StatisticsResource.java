package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.module.Statistics;

import java.util.Optional;
import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.userDBManager;

public class StatisticsResource {
    public static String getStatisticsForDataType(final Request request,
                                                  final Response response) throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.valueOf(request.params("dataType"));
        final Statistic statistic = Statistics.getStatisticsForDataType(dataType);
        response.type("application/json");
        response.status(HttpStatus.OK_200);
        return Statistic.mapper.writeValueAsString(statistic);
    }

    public static String getStatisticsForDataTypeInDemographicGroupOfUser(final Request request,
                                                                          final Response response) throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.valueOf(request.params("dataType"));
        final UUID userId = UUID.fromString(request.params("userId"));
        final Optional<User> user = userDBManager.getUserById(userId);
        if (user.isPresent()) {
            final Statistic statistic =
                    Statistics.getStatisticsInDemographicGroupOfUserForDataType(dataType, user.get());
            response.status(HttpStatus.OK_200);
            response.type("application/json");
            return Statistic.mapper.writeValueAsString(statistic);
        } else {
            response.status(HttpStatus.NOT_FOUND_404);
            return "";
        }
    }
}
