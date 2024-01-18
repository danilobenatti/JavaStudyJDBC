package br.com.ecosensor;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import br.com.ecosensor.repository.ProductRepositoryImpl;
import br.com.ecosensor.repository.Repository;
import br.com.ecosensor.util.ConnectionDB;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) {
		try (Connection conn = ConnectionDB.getInstance()) {
			Repository<Product> prodRepo = new ProductRepositoryImpl();
			logger.log(Level.INFO, conn.getCatalog());
			//SELECT BY ID
			logger.info(prodRepo.searchById(3L));
			//INSERT
			Product productNew = Product.builder().name("Product New").price(3.2F).build();
			productNew.setCategory(Category.builder().id(3L).build());
			prodRepo.save(productNew);
			//UPDATE
			prodRepo.save(Product.builder().id(2L).name("Product2 Update").price(5.6F).build());
			//DELETE
			prodRepo.delete(3L);
			//SELECT ALL
			prodRepo.listAll().forEach(logger::info);
		} catch (Exception ex) {
			logger.log(Level.ERROR, ex);
		}
	}
}
