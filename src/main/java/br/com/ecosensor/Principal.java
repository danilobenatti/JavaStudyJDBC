package br.com.ecosensor;

import br.com.ecosensor.util.ConnectionDB;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.apache.commons.lang3.StringUtils.*;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		try (Connection conn = ConnectionDB.getInstance()) {
			logger.log(Level.INFO, conn.getCatalog());
			try (Statement statement = conn.createStatement()) {
				ResultSet resultSet = statement.executeQuery("SELECT * FROM tbl_product");
				while (resultSet.next()) {
					String msg = joinWith(join(',', SPACE),
							resultSet.getLong("id"),
							resultSet.getString("col_name"),
							resultSet.getString("col_description"),
							resultSet.getFloat("col_price"));
					logger.info(msg);
				}
				resultSet.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
