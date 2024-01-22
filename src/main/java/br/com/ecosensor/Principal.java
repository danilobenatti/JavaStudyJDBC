package br.com.ecosensor;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import br.com.ecosensor.repository.CategoryRepositoryImpl;
import br.com.ecosensor.repository.ProductRepositoryImpl;
import br.com.ecosensor.repository.Repository;
import br.com.ecosensor.util.ConnectionDB;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			Repository<Category> cateRepo = new CategoryRepositoryImpl(conn);
			Repository<Product> prodRepo = new ProductRepositoryImpl(conn);
			//SELECT BY ID
			logger.info(cateRepo.searchById(2L));
			logger.info(prodRepo.searchById(3L));
			//INSERT
			var cNew = Category.builder().name("Category New").build();
			logger.info(cateRepo.save(cNew));//NEW CATEGORY
			var pNew = Product.builder().name("Product New").price(3.2F).build();
			pNew.setSku("24113979639");//24113979638
			pNew.setCategory(Category.builder().id(2L).build());
			logger.info(prodRepo.save(pNew));//NEW PRODUCT
			//UPDATE
			var cUpd = Category.builder().id(2L).name("Category Update").build();
			cateRepo.save(cUpd);//UPDATE CATEGORY
			var pUpd = Product.builder().id(2L).name("Product2 Update").build();
			pUpd.setPrice(5.6F);
			pUpd.setSku("77731421912");
			prodRepo.save(pUpd);//UPDATE PRODUCT
			//DELETE
			cateRepo.delete(1L);
			prodRepo.delete(1L);
			//SELECT ALL
			cateRepo.listAll().forEach(logger::info);
			prodRepo.listAll().forEach(logger::info);
		} catch (SQLException ex2) {
			logger.log(Level.ERROR, ex2);
		}
	}
}
