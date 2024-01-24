package br.com.ecosensor;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import br.com.ecosensor.service.ServiceCatalog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class Principal {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	
	public static void main(String[] args) throws SQLException {
		ServiceCatalog service = new ServiceCatalog();
		//SELECT BY ID
		logger.info(service.getCategoryById(2L));
		logger.info(service.getProductById(3L));
		//INSERT
		var cNew = Category.builder().name("Category New").build();
		logger.info(service.saveCategory(cNew));//NEW CATEGORY
		var pNew = Product.builder().name("Product New").price(3.2F).build();
		pNew.setSku("24113979639");//24113979638
		pNew.setCategory(cNew);
		service.saveProductWithCategory(pNew, cNew);
		logger.info(cNew);//NEW CATEGORY
		logger.info(pNew);//NEW PRODUCT
		//UPDATE
		var cUpd = Category.builder().id(2L).name("Category Update").build();
		service.saveCategory(cUpd);//UPDATE CATEGORY
		var pUpd = Product.builder().id(2L).name("Product2 Update").build();
		pUpd.setPrice(5.6F);
		pUpd.setSku("77731421912");
		logger.info(service.saveProduct(pUpd));//UPDATE PRODUCT
		
		//DELETE
		service.deleteCategory(1L);
		service.deleteProduct(1L);
		//SELECT ALL
		service.listAllCategories().forEach(logger::info);
		service.listAllProducts().forEach(logger::info);
	}
}
