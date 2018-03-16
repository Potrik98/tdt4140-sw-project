package tdt4140.gr1809.app.client;

import tdt4140.gr1809.app.core.model.DataPoint;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DataClient extends BasicClient {
    public DataClient() {
        super();
    }

    public List<DataPoint> getDataPointsForUserId(UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .path("/datapoints")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final List<DataPoint> dataPoints =
                    response.readEntity(new GenericType<List<DataPoint>>() {});
            response.close();
            return dataPoints;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get datapoints of user "
                .concat(userId.toString()));
    }

    public void createDataPoint(DataPoint dataPoint) {
        final Response response = target
                .path("/datapoints")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(dataPoint));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            System.out.println("Response: " + response.getStatus());
            response.close();
            throw new ClientException("Failed to create datapoint.");
        }
        response.close();
    }
}
