package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;
import tdt4140.gr1809.app.server.module.Statistics;

public class StatisticsResource {
    public static String getStatisticsForDataType(final Request request,
                                                  final Response response) throws Exception {
        final DataPoint.DataType dataType = DataPoint.DataType.valueOf(request.params("dataType"));
        final Statistic statistic = Statistics.getStatisticsForDataType(dataType);
        response.status(HttpStatus.OK_200);
        return Statistic.mapper.writeValueAsString(statistic);
    }
}
