package br.com.ecosensor.service;

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
import java.util.List;

public class ServiceCatalog implements Service {
	
	static Logger log = LogManager.getLogger(ServiceCatalog.class);
	
	private final Repository<Product> productRepository;
	
	private final Repository<Category> categoryRepository;
	
	public ServiceCatalog() {
		this.productRepository = new ProductRepositoryImpl();
		this.categoryRepository = new CategoryRepositoryImpl();
	}
	
	@Override
	public List<Product> listAllProducts() throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			productRepository.setConn(conn);
			return productRepository.listAll();
		}
	}
	
	@Override
	public List<Category> listAllCategories() throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			categoryRepository.setConn(conn);
			return categoryRepository.listAll();
		}
	}
	
	@Override
	public Product getProductById(Long id) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			productRepository.setConn(conn);
			return productRepository.searchById(id);
		}
	}
	
	@Override
	public Category getCategoryById(Long id) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			categoryRepository.setConn(conn);
			return categoryRepository.searchById(id);
		}
	}
	
	@Override
	public Product saveProduct(Product product) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			productRepository.setConn(conn);
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			Product productNew = null;
			try {
				productNew = productRepository.save(product);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				log.info(Level.ERROR, ex);
			}
			return productNew;
		}
	}
	
	@Override
	public Category saveCategory(Category category) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			categoryRepository.setConn(conn);
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			Category categoryNew = null;
			try {
				categoryNew = categoryRepository.save(category);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				log.info(Level.ERROR, ex);
			}
			return categoryNew;
		}
	}
	
	@Override
	public void deleteProduct(Long id) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			productRepository.setConn(conn);
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			try {
				productRepository.delete(id);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				log.info(Level.ERROR, ex);
			}
		}
	}
	
	@Override
	public void deleteCategory(Long id) throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			categoryRepository.setConn(conn);
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			try {
				categoryRepository.delete(id);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				log.info(Level.ERROR, ex);
			}
		}
	}
	
	@Override
	public void saveProductWithCategory(Product product, Category category)
			throws SQLException {
		try (Connection conn = ConnectionDB.getDataSourceConnection()) {
			productRepository.setConn(conn);
			categoryRepository.setConn(conn);
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			try {
				Category categoryNew = categoryRepository.save(category);
				product.setCategory(categoryNew);
				productRepository.save(product);
				conn.commit();
			} catch (SQLException ex) {
				conn.rollback();
				log.info(Level.ERROR, ex);
			}
		}
	}
}
