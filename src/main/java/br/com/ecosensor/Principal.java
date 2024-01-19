package br.com.ecosensor;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import br.com.ecosensor.repository.ProductRepositoryImpl;
import br.com.ecosensor.repository.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) {
			Repository<Product> prodRepo = new ProductRepositoryImpl();
			//SELECT BY ID
			logger.info(prodRepo.searchById(3L));
			//INSERT
			Product productNew = Product.builder().name("Product New").price(3.2F).build();
			productNew.setCategory(Category.builder().id(3L).build());
			prodRepo.save(productNew);
			//UPDATE
			Product productUpdate = Product.builder().id(2L).name("Product2 Update").price(5.6F).build();
			prodRepo.save(productUpdate);
			//DELETE
			prodRepo.delete(3L);
			//SELECT ALL
			prodRepo.listAll().forEach(logger::info);
	}
}
