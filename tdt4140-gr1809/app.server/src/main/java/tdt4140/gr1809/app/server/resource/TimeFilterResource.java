package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.TimeFilterDBManager;

import java.io.IOException;

public class TimeFilterResource {
    public static TimeFilterDBManager dbManager;

    public static void init() throws Exception {
        dbManager = new TimeFilterDBManager();
    }

    public static void closeConnection() throws Exception {
        dbManager.closeConnection();
    }

    public static String createTimeFilter(Request req, Response res) throws Exception {
        System.out.println("Create timeFilter");
        try {
            final TimeFilter timeFilter = User.mapper.readValue(req.body(), TimeFilter.class);
                dbManager.createTimeFilter(timeFilter);
            res.status(HttpStatus.CREATED_201);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            res.status(HttpStatus.BAD_REQUEST_400);
        }
        return "";
    }
}
