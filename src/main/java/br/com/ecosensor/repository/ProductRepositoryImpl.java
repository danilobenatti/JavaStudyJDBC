package br.com.ecosensor.repository;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import br.com.ecosensor.util.ConnectionDB;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements Repository<Product> {
	static Logger logger = LogManager.getLogger(ProductRepositoryImpl.class);
	
	private Connection connection() throws SQLException {
		return ConnectionDB.getPoolConnection();
	}
	
	@Override
	public List<Product> listAll() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT p.*, c.* FROM tbl_product AS p INNER JOIN " +
				"tbl_category AS c ON p.id_category = c.id";
		try (Connection conn = connection();
			Statement stmt = conn.prepareStatement(query);
			ResultSet resultSet = stmt.executeQuery(query)) {
			while (resultSet.next()) {
				Product product = getterProduct(resultSet);
				products.add(product);
			}
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
		}
		return products;
	}
	
	@Override
	public Product searchById(Long id) {
		Product product = new Product();
		Category category = new Category();
		String query = "SELECT p.*, c.* FROM tbl_product AS p INNER JOIN " +
				"tbl_category AS c ON p.id_category = c.id WHERE p.id = ?";
		try (Connection conn = connection();
			PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet resultSet = stmt.executeQuery()) {
				if (resultSet.next()) {
					setterProduct(product, category, resultSet);
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
		}
		return product;
	}
	
	@Override
	public void save(Product product) {
		String sql;
		Product p = new Product();
		if (product.getId() != null && product.getId() > 0) {
			p = searchById(product.getId());
			sql = "UPDATE tbl_product SET col_name = ?, col_description = ? ," +
					" col_price = ?, col_date_update = ?, id_category = ?" +
					" WHERE id = " + product.getId();
		} else {
			sql = "INSERT INTO tbl_product (id, col_name, col_description, " +
					"col_price, col_date_create, id_category) VALUES (null, " +
					"?, ?, ?, ?, ?)";
		}
		try (Connection conn = connection();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, product.getName() != null ? product.getName() :
					p.getName());
			stmt.setString(2, product.getDescription() != null ?
					product.getDescription() : p.getDescription());
			stmt.setFloat(3, product.getPrice() != null ? product.getPrice()
					: p.getPrice());
			stmt.setDate(4, Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
			stmt.setLong(5, product.getCategory() != null ?
					product.getCategory().getId() : p.getCategory().getId());
			stmt.executeUpdate();
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
		}
	}
	
	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM tbl_product WHERE id = ?";
		try (Connection conn = connection();
			PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setLong(1, id);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
		}
	}
	
	private static Product getterProduct(ResultSet resultSet) throws SQLException {
		Product product = new Product();
		Category category = new Category();
		makerProduct(product, category, resultSet);
		return product;
	}
	
	private static void setterProduct(Product product, Category category,
									  ResultSet resultSet) throws SQLException {
		makerProduct(product, category, resultSet);
	}
	
	private static void makerProduct(Product product, Category category,
									 ResultSet resultSet) throws SQLException {
		product.setId(resultSet.getLong("id"));
		product.setName(resultSet.getString("p.col_name"));
		product.setDescription(resultSet.getString("p.col_description"));
		product.setPrice(resultSet.getFloat("p.col_price"));
		product.setDateCreate(resultSet.getDate("p.col_date_create"));
		product.setDateUpdate(resultSet.getDate("p.col_date_update"));
		product.setCategory(category);
		category.setId(resultSet.getLong("c.id"));
		category.setName(resultSet.getString("c.col_name"));
		category.setProducts(List.of(product));
	}
}
