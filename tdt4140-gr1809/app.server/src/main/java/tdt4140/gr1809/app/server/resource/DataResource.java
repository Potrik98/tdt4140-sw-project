package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.DataPoint;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.DataDBManager;

import java.io.IOException;
import java.sql.Connection;

public class DataResource {
    protected static DataDBManager dbManager;

    public static void init() throws Exception {
        dbManager = new DataDBManager();
    }

    public static void init(Connection connection) {
        dbManager = new DataDBManager(connection);
    }

    public static String createDataPoint(Request req, Response res) throws Exception {
        System.out.println("Create datapoint");
        try {
            final DataPoint dataPoint = User.mapper.readValue(req.body(), DataPoint.class);
                dbManager.createDataPoint(dataPoint);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }
}
