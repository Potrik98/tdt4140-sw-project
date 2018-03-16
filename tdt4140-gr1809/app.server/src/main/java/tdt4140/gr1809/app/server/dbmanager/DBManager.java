package tdt4140.gr1809.app.server.dbmanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class DBManager {
	protected Connection conn;

    public static void loadCreateScript() throws Exception {
        InputStream input = DBManager.class.getResourceAsStream("dbconnection.properties");
        Properties connectionProps = new Properties();
        try {
            connectionProps.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(connectionProps.getProperty("driver")).newInstance();
        Connection conn = DriverManager.getConnection(
                connectionProps.getProperty("dbname"),
                connectionProps);

        input = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("tdt4140/gr1809/app/server/sqlCreateScript.sql");

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        Statement statement = conn.createStatement();
        statement.execute(out.toString());

        conn.close();
    }

	protected DBManager() throws Exception {
        InputStream input = DBManager.class.getResourceAsStream("dbconnection.properties");
        Properties connectionProps = new Properties();
        try {
            connectionProps.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class.forName(connectionProps.getProperty("driver")).newInstance();
        conn = DriverManager.getConnection(
                connectionProps.getProperty("dbname"),
                connectionProps);
        System.out.println("Opened db connection to " + connectionProps.getProperty("dbname"));
    }

    public void closeConnection() throws SQLException {
	    conn.close();
        System.out.println("Closed db connection");
    }
}
