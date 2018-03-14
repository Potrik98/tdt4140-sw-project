package tdt4140.gr1809.app.server.resource;

import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import tdt4140.gr1809.app.core.model.TimeFilter;
import tdt4140.gr1809.app.core.model.User;
import tdt4140.gr1809.app.server.dbmanager.TimeFilterDBManager;

import java.io.IOException;
import java.sql.Connection;

public class TimeFilterResource {
    protected static TimeFilterDBManager dbManager;

    public static void init() throws Exception {
        dbManager = new TimeFilterDBManager();
    }

    public static void init(Connection connection) {
        dbManager = new TimeFilterDBManager(connection);
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
