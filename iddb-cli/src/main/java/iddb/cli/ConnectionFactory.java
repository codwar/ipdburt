package iddb.cli;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionFactory {

	private static ConnectionFactory instance;
	private Properties props;
	
	private ConnectionFactory() throws IOException {
		props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			throw e;
		}
		try {
			Class.forName(props.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws IOException, SQLException {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return DriverManager.getConnection(instance.props.getProperty("url"), instance.props.getProperty("username"), instance.props.getProperty("password"));
	}
	
}
