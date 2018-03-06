package tdt4140.gr1809.app.server.dbmanager;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBManager {
	protected String dbName = "localhost";
    protected String userName = "root";
	protected String password = "root";
	protected Connection conn;
	
	

	protected DBManager() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        conn = DriverManager.getConnection(
                "jdbc:pu09://" + this.dbName + ":3306/",
                connectionProps);

    }

    protected DBManager(Connection connection) {
	    this.conn = connection;
    }
}
