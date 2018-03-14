package tdt4140.gr1809.app.client;

import tdt4140.gr1809.app.core.model.TimeFilter;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

public class TimeFilterClient extends BasicClient {
    public TimeFilterClient() {
        super();
    }

    public TimeFilterClient(String path) {
        super(path);
    }

    public List<TimeFilter> getTimeFiltersForUserId(UUID userId) {
        final Response response = target
                .path("/user/")
                .path(userId.toString())
                .path("/timefilters")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
        if (response.getStatus() == HttpURLConnection.HTTP_OK) {
            final List<TimeFilter> timeFilters =
                    response.readEntity(new GenericType<List<TimeFilter>>() {});
            response.close();
            return timeFilters;
        }
        System.out.println("Response: " + response.getStatus());
        response.close();
        throw new ClientException("Failed to get timefilters of user "
                .concat(userId.toString()));
    }

    public void createTimeFilter(TimeFilter timeFilter) {
        final Response response = target
                .path("/timefilters")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(timeFilter));
        if (response.getStatus() != HttpURLConnection.HTTP_CREATED) {
            System.out.println("Response: " + response.getStatus());
            response.close();
            throw new ClientException("Failed to create timefilter.");
        }
        response.close();
    }
}
