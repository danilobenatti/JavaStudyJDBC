package br.com.ecosensor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.joinWith;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionDB {
	
	private static final String USER = "root";
	private static final String PASSWORD = "123456";
	private static final String ENCODING = "useUnicode=true;characterEncoding=UTF-8";
	private static final String TIMEZONE = "useTimezone=true;serverTimezone=America/Sao_Paulo";
	private static final String URL = "jdbc:mysql://localhost:3306/java_course";
	
	private static BasicDataSource dataSource;
	
	static Logger log = LogManager.getLogger(ConnectionDB.class);
	
	public static BasicDataSource getDataSource() {
		if (dataSource == null || dataSource.isClosed()) {
			dataSource = new BasicDataSource();
			dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
			dataSource.setUrl(URL);
			dataSource.setUsername(USER);
			dataSource.setPassword(PASSWORD);
			dataSource.setInitialSize(3);
			dataSource.setMinIdle(3);
			dataSource.setMaxIdle(8);
			dataSource.setMaxTotal(8);
			dataSource.setConnectionProperties(joinWith(";", ENCODING, TIMEZONE));
			dataSource.setDefaultSchema("java_course");
			dataSource.setDefaultAutoCommit(false);
		}
		log.info(() -> join("Start connection: ", dataSource.getDefaultCatalog()));
		return dataSource;
	}
	
	public static Connection getDataSourceConnection() throws SQLException {
		return getDataSource().getConnection();
	}
}
