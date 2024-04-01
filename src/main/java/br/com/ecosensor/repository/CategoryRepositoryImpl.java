package br.com.ecosensor.repository;

import br.com.ecosensor.model.Category;
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
public class CategoryRepositoryImpl implements Repository<Category> {
	
	static Logger log = LogManager.getLogger(CategoryRepositoryImpl.class);
	
	private Connection conn;
	
	public CategoryRepositoryImpl(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public List<Category> listAll() {
		List<Category> categories = new ArrayList<>();
		String query = "SELECT * FROM tbl_category";
		try (Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Category category = getterCategory(rs);
				categories.add(category);
			}
		} catch (SQLException ex) {
			log.log(Level.ERROR, ex);
		}
		return categories;
	}
	
	@Override
	public Category searchById(Long id) {
		Category category = new Category();
		String query = "SELECT c.* FROM tbl_category AS c WHERE c.id = ?";
		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setLong(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					setterCategory(category, rs);
				}
			}
		} catch (SQLException ex) {
			log.log(Level.ERROR, ex);
		}
		return category;
	}
	
	@Override
	public Category save(Category category) {
		String sql;
		Category c = new Category();
		if (category.getId() != null && category.getId() > 0) {
			c = searchById(category.getId());
			sql = "UPDATE tbl_category SET col_name = ?, col_date_update = ? "
					+ "WHERE id = " + category.getId();
		} else {
			sql = "INSERT INTO tbl_category (id, col_name, col_date_create) "
					+ "VALUE (null, ?, ?)";
		}
		try (PreparedStatement stmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS)) {
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			saveCategory(category, c, stmt);
			stmt.executeUpdate();
			if (category.getId() == null) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					long id = rs.getLong(1);
					category.setId(id);
					log.info(() -> joinWith(SPACE, "New category id:", id));
				}
			}
			conn.commit();
		} catch (SQLException ex) {
			log.log(Level.ERROR, ex);
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.log(Level.ERROR, e);
			}
		}
		return category;
	}
	
	@Override
	public void delete(Long id) {
		try (Statement stmt = conn.createStatement()) {
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			stmt.execute("DELETE FROM tbl_category WHERE id = " + id);
			conn.commit();
		} catch (SQLException ex) {
			log.log(Level.ERROR, ex);
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.log(Level.ERROR, e);
			}
		}
	}
	
	private static Category getterCategory(ResultSet resultSet)
			throws SQLException {
		Category category = new Category();
		makerCategory(category, resultSet);
		return category;
	}
	
	private static void setterCategory(Category category, ResultSet resultSet)
			throws SQLException {
		makerCategory(category, resultSet);
	}
	
	private static void makerCategory(Category category, ResultSet resultSet)
			throws SQLException {
		category.setId(resultSet.getLong("id"));
		category.setName(resultSet.getString("col_name"));
		category.setDateCreate(resultSet.getDate("col_date_create"));
		category.setDateUpdate(resultSet.getDate("col_date_update"));
	}
	
	private static void saveCategory(Category c1, Category c2,
			PreparedStatement stmt) throws SQLException {
		stmt.setString(1, c1.getName() != null ? c1.getName() : c2.getName());
		stmt.setDate(2, Date.valueOf(LocalDate.now(ZoneId.systemDefault())));
	}
}
