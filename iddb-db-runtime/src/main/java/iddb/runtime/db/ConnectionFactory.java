package iddb.runtime.db;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConnectionFactory {

	private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private static ConnectionFactory instance;
	private Properties props;
	
	private ConnectionFactory() throws IOException {
		log.debug("Initializing ConnectionFactory");
		props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			log.error("Unable to load db.properties. {}", e.getMessage());
			throw e;
		}
		loadDriver();
	}

	private void loadDriver() throws IOException {
		try {
			Class.forName(props.getProperty("driver"));
		} catch (ClassNotFoundException e) {
			log.error("Unable to load driver {}", props.getProperty("driver"));
			throw new IOException(e);
		}
	}
	
	public static Connection getConnection() throws IOException {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return null;
	}
}
