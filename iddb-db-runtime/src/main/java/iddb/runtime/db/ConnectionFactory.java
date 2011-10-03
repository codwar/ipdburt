package iddb.runtime.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConnectionFactory {

	private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);
	
	private static ConnectionFactory instance;
	private DataSource masterDataSource;
	private DataSource slaveDataSource;
	
	private ConnectionFactory() throws IOException {
		log.debug("Initializing ConnectionFactory");
		Properties props;
		props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			log.error("Unable to load db.properties. {}", e.getMessage());
			throw e;
		}
		masterDataSource = null;
		slaveDataSource = null;
		initializeMasterDatasource(props);
		initializeSlaveDatasource(props);
	}

	private void initializeMasterDatasource(Properties props) {
		log.debug("Initializing Master DataSource");
		PoolProperties p = new PoolProperties();
        p.setUrl(props.getProperty("url"));
        p.setDriverClassName(props.getProperty("driver"));
        p.setUsername(props.getProperty("username"));
        p.setPassword(props.getProperty("password"));
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(5);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        masterDataSource = new DataSource(p);
	}

	private void initializeSlaveDatasource(Properties props) {
		if (props.containsKey("slave.url")) {
			log.debug("Initializing Slave DataSource");
			PoolProperties p = new PoolProperties();
	        p.setUrl(props.getProperty("slave.url"));
	        p.setDriverClassName(props.getProperty("slave.driver"));
	        p.setUsername(props.getProperty("slave.username"));
	        p.setPassword(props.getProperty("slave.password"));
	        p.setJmxEnabled(true);
	        p.setTestWhileIdle(false);
	        p.setTestOnBorrow(true);
	        p.setValidationQuery("SELECT 1");
	        p.setTestOnReturn(false);
	        p.setValidationInterval(30000);
	        p.setTimeBetweenEvictionRunsMillis(30000);
	        p.setMaxActive(100);
	        p.setInitialSize(5);
	        p.setMaxWait(10000);
	        p.setRemoveAbandonedTimeout(60);
	        p.setMinEvictableIdleTimeMillis(30000);
	        p.setMinIdle(10);
	        p.setLogAbandoned(true);
	        p.setRemoveAbandoned(true);
	        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
	        slaveDataSource = new DataSource(p);			
		} else {
			log.trace("No Slave DataSource specified.");
		}

	}
	
	public static Connection getMasterConnection() throws IOException, SQLException {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		return instance.masterDataSource.getConnection();
	}

	public static Connection getSecondaryConnection() throws IOException, SQLException {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		if (instance.slaveDataSource != null) {
			return instance.slaveDataSource.getConnection();
		} else {
			return instance.masterDataSource.getConnection();	
		}
	}	
	
}
