package tdt4140.gr1809.app.client;

import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.Statistic;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

public class StatisticsClient extends BasicClient {
    public StatisticsClient() {
        super();
    }

    public Statistic getStatisticsForDataType(final DataPoint.DataType dataType) {
        final Response response = target
                .path("/statistics/")
                .path(dataType.name())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final Statistic statistic =
                    response.readEntity(Statistic.class);
            response.close();
            return statistic;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get statistic of datatype "
                .concat(dataType.name()));
    }
}
