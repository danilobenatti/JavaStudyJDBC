package br.com.ecosensor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static org.apache.commons.lang3.StringUtils.join;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionDB {
	
	private static final String USER = "root";
	private static final String PASSWORD = "123456";
	private static final String ENCODING = "UTF8";
	private static final String URL = "jdbc:mysql://localhost:3306/java_course?useTimezone=true&serverTimezone=America/Sao_Paulo";
	private static BasicDataSource pool;
	static Logger logger = LogManager.getLogger(ConnectionDB.class);
	
	public static BasicDataSource getPool() {
		if (pool == null) {
			pool = new BasicDataSource();
			pool.setDriverClassName("com.mysql.cj.jdbc.Driver");
			pool.setUrl(URL);
			pool.setUsername(USER);
			pool.setPassword(PASSWORD);
			pool.setInitialSize(3);
			pool.setMinIdle(3);
			pool.setMaxIdle(8);
			pool.setMaxTotal(8);
			pool.setConnectionProperties(ENCODING);
		}
		var msg = join("Start connection: ", pool.getDefaultCatalog());
		logger.info(msg);
		return pool;
	}
	
	public static Connection getPoolConnection() throws SQLException {
		return getPool().getConnection();
	}
}
