package tdt4140.gr1809.app.server.dbmanager;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public abstract class DBManager {
	
    protected Object userName;
	protected Object password;
	protected Connection conn;
	protected String serverName;
	protected String portNumber;
	protected String dbName;
	

	protected DBManager() throws SQLException {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", this.userName);
        connectionProps.put("password", this.password);
        conn = DriverManager.getConnection(
                "jdbc:" + this.dbName + "://" +
                this.serverName +
                ":" + this.portNumber + "/",
                connectionProps);

    }
}
