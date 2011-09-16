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
	private Properties props;
	private DataSource dataSource;
	
	private ConnectionFactory() throws IOException {
		log.debug("Initializing ConnectionFactory");
		props = new Properties();
		try {
			props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			log.error("Unable to load db.properties. {}", e.getMessage());
			throw e;
		}
		
		PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:mysql://166.40.231.124:3306/test?autoReconnect=true");
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername("test");
        p.setPassword("test");
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
        
        dataSource = new DataSource(p);
		//loadDriver();
	}

	private void loadDriver() throws IOException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log.error("Unable to load mysql driver");
			throw new IOException(e);
		}
	}
	
	public static Connection getConnection() throws IOException, SQLException {
		if (instance == null) {
			instance = new ConnectionFactory();
		}
		//return DriverManager.getConnection("jdbc:mysql://localhost/test", "test", "test");
		return instance.dataSource.getConnection();
	}
}
