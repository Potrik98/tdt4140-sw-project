package tdt4140.gr1809.app.server.dbmanager;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBManager {
	protected String dbName = "pu09";
    protected String userName = "root";
	protected String password = "root";
	protected Connection conn;

	protected DBManager() throws Exception {
        Connection conn = null;
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + dbName,
                connectionProps);

    }

    protected DBManager(Connection connection) {
	    this.conn = connection;
    }
}
