package br.com.ecosensor.service;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;

import java.sql.SQLException;
import java.util.List;

public interface Service {
	List<Product> listAllProducts() throws SQLException;
	
	List<Category> listAllCategories() throws SQLException;
	
	Product getProductById(Long id) throws SQLException;
	
	Category getCategoryById(Long id) throws SQLException;
	
	Product saveProduct(Product product) throws SQLException;
	
	Category saveCategory(Category category) throws SQLException;
	
	void deleteProduct(Long id) throws SQLException;
	
	void deleteCategory(Long id) throws SQLException;
	
	void saveProductWithCategory(Product product, Category category) throws SQLException;
}
