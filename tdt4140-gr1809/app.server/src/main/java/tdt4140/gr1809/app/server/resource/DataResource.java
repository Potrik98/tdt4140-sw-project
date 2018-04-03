package tdt4140.gr1809.app.server.resource;

import com.google.common.collect.ImmutableList;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.DataDBManager;
import tdt4140.gr1809.app.server.module.Analyzer;
import tdt4140.gr1809.app.server.module.Filter;

import java.io.IOException;
import java.util.List;

import static tdt4140.gr1809.app.core.util.StreamUtils.uncheckRun;

public class DataResource {
    protected static DataDBManager dbManager;
    private static Filter filter;
    private static Analyzer analyzer;

    public static void init() throws Exception {
        dbManager = new DataDBManager();
        filter = new Filter(TimeFilterResource.dbManager);
        analyzer = new Analyzer(UserResource.dbManager, NotificationResource.dbManager);
    }

    public static void closeConnection() throws Exception {
        dbManager.closeConnection();
    }

    public static String createDataPoint(Request req, Response res) throws Exception {
        System.out.println("Create datapoint");
        try {
            final DataPoint dataPoint = User.mapper.readValue(req.body(), DataPoint.class);
            final List<DataPoint> dataPoints =
                    filter.filterDataPoints(ImmutableList.of(dataPoint));
            analyzer.analyzeDataPoints(dataPoints);
            dataPoints.forEach(dp -> uncheckRun(() -> dbManager.createDataPoint(dp)));
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }
}
