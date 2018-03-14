package tdt4140.gr1809.app.server.resource;

import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.DataDBManager;
import tdt4140.gr1809.app.server.module.Filter;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class DataResource {
    protected static DataDBManager dbManager;
    private static Filter filter;

    public static void init() throws Exception {
        dbManager = new DataDBManager();
        filter = new Filter(TimeFilterResource.dbManager);
    }

    public static void init(Connection connection) {
        dbManager = new DataDBManager(connection);
        filter = new Filter(TimeFilterResource.dbManager);
    }

    public static String createDataPoint(Request req, Response res) throws Exception {
        System.out.println("Create datapoint");
        try {
            final DataPoint dataPoint = User.mapper.readValue(req.body(), DataPoint.class);
            final List<DataPoint> dataPoints =
                    filter.filterDataPoints(ImmutableList.of(dataPoint));
            if (!dataPoints.isEmpty()) {
                dbManager.createDataPoint(dataPoint);
            }
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }
}
