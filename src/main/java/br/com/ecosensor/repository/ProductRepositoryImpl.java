package br.com.ecosensor.repository;

import br.com.ecosensor.model.Product;
import br.com.ecosensor.util.ConnectionDB;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements Repository<Product> {
	
	private Connection connection() throws SQLException {
		return ConnectionDB.getInstance();
	}
	
	@Override
	public List<Product> listAll() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM tbl_product";
		try (Statement stmt = connection().prepareStatement(query)) {
			try (ResultSet resultSet = stmt.executeQuery(query)) {
				while (resultSet.next()) {
					Product product = getProduct(resultSet);
					products.add(product);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return products;
	}
	
	@Override
	public Product searchById(Long id) {
		Product product = new Product();
		String query = "SELECT * FROM tbl_product WHERE id = ?";
		try (PreparedStatement stmt = connection().prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet resultSet = stmt.executeQuery()) {
				if (resultSet.next()) {
					setProduct(product, resultSet);
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return product;
	}
	
	@Override
	public void save(Product product) {
		String sql;
		Product p = new Product();
		if (product.getId() != null) {
			p = searchById(product.getId());
			sql = "UPDATE tbl_product SET col_name = ?, col_description = ? ," +
					" col_price = ?, col_date_update = ? WHERE id = " + p.getId();
		} else {
			sql = "INSERT INTO tbl_product (id, col_name, col_description, " +
					"col_price, col_date_create) VALUES (null, ?, ?, ?, ?)";
		}
		try (PreparedStatement stmt = connection().prepareStatement(sql)) {
			stmt.setString(1, product.getName() != null ? product.getName() :
					p.getName());
			stmt.setString(2, product.getDescription() != null ?
					product.getDescription() : p.getDescription());
			stmt.setFloat(3, product.getPrice() != null ? product.getPrice()
					: p.getPrice());
			stmt.setDate(4, Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void delete(Long id) {
		String sql = "DELETE FROM tbl_product WHERE id = ?";
		try (PreparedStatement stmt = connection().prepareStatement(sql)) {
			stmt.setLong(1, id);
			stmt.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private static Product getProduct(ResultSet resultSet) throws SQLException {
		Product product = new Product();
		makeResultSet(product, resultSet);
		return product;
	}
	
	private static void setProduct(Product product, ResultSet resultSet) throws SQLException {
		makeResultSet(product, resultSet);
	}
	
	private static void makeResultSet(Product product, ResultSet resultSet) throws SQLException {
		product.setId(resultSet.getLong("id"));
		product.setName(resultSet.getString("col_name"));
		product.setDescription(resultSet.getString("col_description"));
		product.setPrice(resultSet.getFloat("col_price"));
		product.setDateCreate(resultSet.getDate("col_date_create"));
		product.setDateUpdate(resultSet.getDate("col_date_update"));
	}
}
