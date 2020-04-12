package ua.nure.yeshenko.SummaryTask.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class ProductDAO extends DBManager {

	private static final Logger LOG = Logger.getLogger(ProductDAO.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static ProductDAO instance;

	public static synchronized ProductDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new ProductDAO();
		}
		return instance;
	}

	private ProductDAO() throws DBException {
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			// root - the name of data source
			ds = (DataSource) envContext.lookup("jdbc/root");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
	}

	// //////////////////////////////////////////////////////////
	// SQL queries
	// //////////////////////////////////////////////////////////

	private static final String SQL_FIND_PRODUCT_BY_ID = "SELECT * FROM products WHERE id=?";
	
	private static final String SQL_FIND_PRODUCT_BY_NAME = "SELECT * FROM products WHERE name LIKE ?";

	private static final String SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE = "SELECT * FROM products WHERE gender=? AND type=?";

	private static final String SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE_PRICE_FROM_TO = "SELECT * FROM products WHERE gender=? AND type=? AND price BETWEEN ? AND ?";

	private static final String SQL_UPDATE_PRODUCT_QUANTITY = "UPDATE products SET quantity=? WHERE id=?";

	private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET name=?, type=?, size=?, gender=?, price=?, quantity=? WHERE id=?";

	/**
	 * Returns a product with the given identifier.
	 * 
	 * @param id Product identifier.
	 * @return Product entity.
	 * @throws DBException
	 */
	public Product findProduct(long id) throws DBException {
		Product product = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			ProductMapper mapper = new ProductMapper();
			pstmt = con.prepareStatement(SQL_FIND_PRODUCT_BY_ID);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				product = mapper.mapRow(rs);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
		} finally {
			close(con);
		}
		return product;
	}
	
	/**
	 * Returns a products with the given name.
	 * 
	 * @param name Product name.
	 * @return Product entity.
	 * @throws DBException
	 */
	public List<Product> findProduct(String name) throws DBException {
		List<Product> products = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			ProductMapper mapper = new ProductMapper();
			pstmt = con.prepareStatement(SQL_FIND_PRODUCT_BY_NAME);
			pstmt.setString(1, "%" + name.trim() + "%");
			LOG.trace(pstmt.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				products.add(mapper.mapRow(rs));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCTS_BY_NAME, ex);
		} finally {
			close(con);
		}
		return products;
	}

	/**
	 * Returns all product by gender and type.
	 * 
	 * @param gender selected Gender
	 *
	 * @param type   selected Type
	 *
	 * @return List of Product entities.
	 * 
	 * @throws DBException
	 */
	public List<Product> findAllProduct(Gender gender, Type type) throws DBException {
		List<Product> products = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			ProductMapper mapper = new ProductMapper();
			pstmt = con.prepareStatement(SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE);
			int k = 1;
			pstmt.setInt(k++, gender.ordinal());
			pstmt.setInt(k++, type.ordinal());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				products.add(mapper.mapRow(rs));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
		} finally {
			close(con);
		}

		return products;
	}

	/**
	 * Returns all product by gender and type from price to price.
	 * 
	 * @param from   selected from price
	 * 
	 * @param to     selected to price
	 * 
	 * @param gender selected Gender
	 *
	 * @param type   selected Type
	 *
	 * @return List of Product entities.
	 * 
	 * @throws DBException
	 */
	public List<Product> findAllProduct(int from, int to, Gender gender, Type type) throws DBException {
		List<Product> products = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			ProductMapper mapper = new ProductMapper();
			pstmt = con.prepareStatement(SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE_PRICE_FROM_TO);
			int k = 1;
			pstmt.setInt(k++, gender.ordinal());
			pstmt.setInt(k++, type.ordinal());
			pstmt.setInt(k++, from);
			pstmt.setInt(k++, to);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				products.add(mapper.mapRow(rs));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
		} finally {
			close(con);
		}
		return products;
	}

	/**
	 * Update product.
	 * 
	 * @param product product to update.
	 * 
	 * @param bool    is to change quantity
	 * 
	 * @throws DBException
	 */
	public void updateProduct(Product product, boolean bool) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_QUANTITY);
			if (bool) {
				pstmt.setInt(1, product.getQuantity() - 1);
			} else {
				pstmt.setInt(1, product.getQuantity() + 1);
			}
			pstmt.setLong(2, product.getId());
			pstmt.executeUpdate();
			pstmt.close();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Update product.
	 * 
	 * @param product product to update.
	 * @throws DBException
	 */
	public void updateProduct(Product product) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT);
			int k = 1;
			pstmt.setString(k++, product.getName());
			pstmt.setInt(k++, product.getType().ordinal());
			pstmt.setInt(k++, product.getSize());
			pstmt.setInt(k++, product.getGender().ordinal());
			pstmt.setInt(k++, product.getPrice());
			pstmt.setInt(k++, product.getQuantity());
			pstmt.setLong(k++, product.getId());
			pstmt.executeUpdate();
			pstmt.close();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Insert product to db
	 * 
	 * @param product product to insert
	 * 
	 * @return success
	 * 
	 * @throws DBException
	 */
	public boolean insertProduct(Product product) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement("INSERT INTO products VALUES(DEFAULT, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			int k = 1;
			pstmt.setString(k++, product.getName());
			pstmt.setInt(k++, product.getType().ordinal());
			pstmt.setInt(k++, product.getSize());
			pstmt.setInt(k++, product.getGender().ordinal());
			pstmt.setInt(k++, product.getPrice());
			pstmt.setInt(k++, product.getQuantity());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
		}
		return true;
	}

	/**
	 * Extracts a product from the result set row.
	 */
	private static class ProductMapper implements EntityMapper<Product> {

		@Override
		public Product mapRow(ResultSet rs) {
			try {
				Product product = new Product();
				product.setId(rs.getLong(Fields.ENTITY__ID));
				product.setName(rs.getString(Fields.PRODUCT__NAME));
				product.setType(Type.values()[rs.getInt(Fields.PRODUCT__TYPE)]);
				product.setSize(rs.getInt(Fields.PRODUCT__SIZE));
				product.setPrice(rs.getInt(Fields.PRODUCT__PRICE));
				product.setGender(Gender.values()[rs.getInt(Fields.PRODUCT__GENDER)]);
				product.setQuantity(rs.getInt(Fields.PRODUCT__QUANTITY));
				return product;
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
