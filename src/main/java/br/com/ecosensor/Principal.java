package br.com.ecosensor;

import br.com.ecosensor.model.Product;
import br.com.ecosensor.repository.ProductRepositoryImpl;
import br.com.ecosensor.repository.Repository;
import br.com.ecosensor.util.ConnectionDB;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) {
		try (Connection conn = ConnectionDB.getInstance()) {
			Repository<Product> repo = new ProductRepositoryImpl();
			logger.log(Level.INFO, conn.getCatalog());
			//SELECT BY ID
			logger.info(repo.searchById(3L));
			//INSERT
			repo.save(Product.builder().name("Product New").price(3.2F).build());
			//UPDATE
			repo.save(Product.builder().id(2L).name("Product2 Update").price(5.6F).build());
			//DELETE
			repo.delete(3L);
			//SELECT ALL
			repo.listAll().forEach(p -> logger.info(p.toString()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
