package br.com.ecosensor;

import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.*;

public class Principal {
	
	private static final String USER = "root";
	private static final String PASSWORD = "123456";
	private static final String ENCODING = "UTF8";
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/java_course?useTimezone=true&serverTimezone=America/Sao_Paulo";
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		Properties props = new Properties();
		props.put("user", USER);
		props.put("password", PASSWORD);
		props.put("encoding", ENCODING);
		
		Class.forName(DRIVER);
		try (Connection conn = DriverManager.getConnection(URL, props)) {
			System.out.println(conn.getCatalog());
			try (Statement statement = conn.createStatement()) {
				ResultSet resultSet = statement.executeQuery("SELECT * FROM tbl_product");
				while (resultSet.next()) {
					String msg = joinWith(join(',', SPACE), resultSet.getString("col_name"),
							resultSet.getString("col_description"));
					System.out.println(msg);
				}
				resultSet.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
