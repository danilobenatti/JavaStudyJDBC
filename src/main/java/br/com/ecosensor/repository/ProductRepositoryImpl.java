package br.com.ecosensor.repository;

import br.com.ecosensor.model.Category;
import br.com.ecosensor.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.joinWith;

@Getter
@Setter
@NoArgsConstructor
public class ProductRepositoryImpl implements Repository<Product> {
	
	static Logger logger = LogManager.getLogger(ProductRepositoryImpl.class);
	
	private Connection conn;
	
	public ProductRepositoryImpl(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Product> listAll() {
		List<Product> products = new ArrayList<>();
		String query = "SELECT p.*, c.* FROM tbl_product AS p INNER JOIN " +
				"tbl_category AS c ON p.id_category = c.id";
		try (Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Product product = getterProduct(rs);
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
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					setterProduct(product, category, rs);
				}
			}
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
		}
		return product;
	}
	
	@Override
	public Product save(Product product) {
		String sql;
		Product p = new Product();
		if (product.getId() != null && product.getId() > 0) {
			p = searchById(product.getId());
			sql = "UPDATE tbl_product SET col_name = ?, col_description = ?, " +
					"col_price = ?, col_date_update = ?, id_category = ?, " +
					"col_sku = ? WHERE id = " + product.getId();
		} else {
			sql = "INSERT INTO tbl_product (id, col_name, col_description, " +
					"col_price, col_date_create, id_category, col_sku) VALUES" +
					" (null, ?, ?, ?, ?, ?, ?)";
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql,
					 Statement.RETURN_GENERATED_KEYS)) {
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			saveProduct(product, p, stmt);
			stmt.executeUpdate();
			if (product.getId() == null) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					long id = rs.getLong(1);
					product.setId(id);
					logger.info(() -> joinWith(SPACE, "New product id:", id));
				}
			}
			conn.commit();
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.log(Level.ERROR, e);
			}
		}
		return product;
	}
	
	@Override
	public void delete(Long id) {
		try (Statement stmt = conn.createStatement()) {
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			stmt.executeUpdate("DELETE FROM tbl_product WHERE id = " + id);
			conn.commit();
		} catch (SQLException ex) {
			logger.log(Level.ERROR, ex);
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.log(Level.ERROR, e);
			}
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
		product.setSku(resultSet.getString("p.col_sku"));
		product.setCategory(category);
		category.setId(resultSet.getLong("c.id"));
		category.setName(resultSet.getString("c.col_name"));
		category.setDateCreate(resultSet.getDate("c.col_date_create"));
		category.setDateUpdate(resultSet.getDate("c.col_date_update"));
		category.setProducts(List.of(product));
	}
	
	private static void saveProduct(Product p1, Product p2,
									PreparedStatement stmt) throws SQLException {
		stmt.setString(1, p1.getName() != null ? p1.getName() : p2.getName());
		stmt.setString(2, p1.getDescription() != null ? p1.getDescription() :
				p2.getDescription());
		stmt.setFloat(3, p1.getPrice() != null ? p1.getPrice() : p2.getPrice());
		stmt.setDate(4, Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
		stmt.setLong(5, p1.getCategory() != null ? p1.getCategory().getId() :
				p2.getCategory().getId());
		stmt.setString(6, p1.getSku() != null ? p1.getSku() : p2.getSku());
	}
}
