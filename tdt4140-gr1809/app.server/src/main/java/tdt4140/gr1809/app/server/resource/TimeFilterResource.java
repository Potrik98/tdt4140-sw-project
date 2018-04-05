package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;

import java.io.IOException;
import java.util.UUID;

import static tdt4140.gr1809.app.server.dbmanager.DBManager.timeFilterDBManager;

public class TimeFilterResource {
    public static String createTimeFilter(Request req, Response res) throws Exception {
        System.out.println("Create timeFilter");
        try {
            final TimeFilter timeFilter = User.mapper.readValue(req.body(), TimeFilter.class);
                timeFilterDBManager.createTimeFilter(timeFilter);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }

    public static String deleteTimefilter(final Request request,
                                          final Response response) throws Exception {
        final UUID timefilterId = UUID.fromString(request.params("timefilterId"));
        System.out.println("Delete timefilter id : " + timefilterId);
        timeFilterDBManager.deleteTimeFilter(timefilterId);
        response.status(HttpStatus.NO_CONTENT_204);
        return "";
    }
}
