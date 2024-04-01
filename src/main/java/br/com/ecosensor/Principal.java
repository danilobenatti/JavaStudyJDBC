package br.com.ecosensor;

import br.com.ecosensor.service.ServiceCatalog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static br.com.ecosensor.model.Category.categoryBuilder;
import static br.com.ecosensor.model.Product.productBuilder;

import java.sql.SQLException;

public class Principal {
	
	static Logger log = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) throws SQLException {
		
		ServiceCatalog service = new ServiceCatalog();
		
		// SELECT BY ID
		log.info(service.getCategoryById(2L));
		log.info(service.getProductById(3L));
		
		// INSERT
		var cNew = categoryBuilder().withName("Category New").build();
		log.info(service.saveCategory(cNew));// NEW CATEGORY
		
		var pNew = productBuilder().withName("Product New").withPrice(3.2F)
				.withCategory(cNew).build();
		pNew.setSku("24113979639");// 24113979638
		
		service.saveProductWithCategory(pNew, cNew);
		
		log.info(cNew);// NEW CATEGORY
		log.info(pNew);// NEW PRODUCT
		
		// UPDATE
		var cUpd = categoryBuilder().withId(2L).withName("Category Update")
				.build();
		service.saveCategory(cUpd);// UPDATE CATEGORY
		
		var pUpd = productBuilder().withId(2L).withName("Product2 Update")
				.withPrice(5.6F).withSku("77731421912").build();
		
		log.info(service.saveProduct(pUpd));// UPDATE PRODUCT
		
		// DELETE
		service.deleteCategory(1L);
		service.deleteProduct(1L);
		
		// SELECT ALL
		service.listAllCategories().forEach(log::info);
		service.listAllProducts().forEach(log::info);
	}
}
