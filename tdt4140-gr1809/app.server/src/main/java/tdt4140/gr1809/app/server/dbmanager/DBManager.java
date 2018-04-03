package tdt4140.gr1809.app.server.dbmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

public abstract class DBManager {
	protected Connection conn;

	public static AccessDBManager accessDBManager;
	public static DataDBManager dataDBManager;
	public static NotificationDBManager notificationDBManager;
	public static ServiceProviderDBManager serviceProviderDBManager;
	public static TimeFilterDBManager timeFilterDBManager;
	public static UserDBManager userDBManager;

	private static Connection connectionFromProperties() throws Exception {
        InputStream input = DBManager.class.getResourceAsStream("dbconnection.properties");
        Properties connectionProps = new Properties();
        try {
            connectionProps.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(connectionProps.getProperty("driver")).newInstance();
        System.out.println("Opening db connection to " + connectionProps.getProperty("dbname"));
        Connection connection = DriverManager.getConnection(
                connectionProps.getProperty("dbname"),
                connectionProps);
        if (Objects.isNull(connection.getCatalog())
                || connection.getCatalog().isEmpty()) {
            connection.setCatalog(connectionProps.getProperty("catalog"));
        }
        if (Objects.isNull(connection.getSchema())
                || connection.getSchema().isEmpty()) {
            connection.setSchema(connectionProps.getProperty("schema"));
        }
        return connection;
    }

    public static void loadCreateScript() throws Exception {
	    Connection conn = connectionFromProperties();

        InputStream input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        System.out.println("Running create script");
        Statement statement = conn.createStatement();
        statement.execute(out.toString());

        conn.close();
        System.out.println("Closed db connection");
    }

    public static void initDBManagers() throws Exception {
	    accessDBManager = new AccessDBManager();
	    dataDBManager = new DataDBManager();
	    notificationDBManager = new NotificationDBManager();
	    serviceProviderDBManager = new ServiceProviderDBManager();
	    timeFilterDBManager = new TimeFilterDBManager();
	    userDBManager = new UserDBManager();
    }

    public static void closeDBConnections() throws Exception {
	    accessDBManager.closeConnection();
	    dataDBManager.closeConnection();
	    notificationDBManager.closeConnection();
	    serviceProviderDBManager.closeConnection();
	    timeFilterDBManager.closeConnection();
	    userDBManager.closeConnection();
    }

	protected DBManager() throws Exception {
        conn = connectionFromProperties();
    }

    public void closeConnection() throws SQLException {
	    conn.close();
        System.out.println("Closed db connection");
    }
}
