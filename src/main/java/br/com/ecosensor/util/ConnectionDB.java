package br.com.ecosensor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionDB {
	
	private static final String USER = "root";
	private static final String PASSWORD = "123456";
	private static final String ENCODING = "UTF8";
	private static final String URL = "jdbc:mysql://localhost:3306/java_course?useTimezone=true&serverTimezone=America/Sao_Paulo";
	private static Connection connection;
	
	public static Connection getInstance() throws SQLException {
		
		Properties props = new Properties();
		props.put("user", USER);
		props.put("password", PASSWORD);
		props.put("encoding", ENCODING);
		
		if (connection == null) {
			connection = DriverManager.getConnection(URL, props);
		}
		return connection;
	}
}
