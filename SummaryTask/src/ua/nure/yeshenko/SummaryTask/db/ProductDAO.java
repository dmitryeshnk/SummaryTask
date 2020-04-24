package ua.nure.yeshenko.SummaryTask.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class ProductDAO {
	private static final String SQL_FIND_PRODUCT_BY_ID = "SELECT * FROM products WHERE id=?";

	private static final String SQL_FIND_PRODUCT_BY_NAME = "SELECT * FROM products WHERE name LIKE ?";

	private static final String SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE_PRICE_FROM_TO = "SELECT * FROM products WHERE gender=? AND type=? AND price BETWEEN ? AND ?";

	private static final String SQL_UPDATE_PRODUCT_QUANTITY = "UPDATE products SET quantity=? WHERE id=?";

	private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET name=?, type=?, size=?, gender=?, price=?, quantity=?, image=? WHERE id=?";

	private static final String SQL_INSERT_PRODUCT = "INSERT INTO products VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";

	/**
	 * Returns a product with the given identifier.
	 * 
	 * @param id Product identifier.
	 * @return Product entity.
	 * @throws DBException
	 */
	public Product findProduct(long id) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_PRODUCT_BY_ID)) {
			ProductMapper mapper = new ProductMapper();
			pstmt.setLong(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapper.mapRow(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
		}
		return null;
	}

	/**
	 * Returns a products with the given name.
	 * 
	 * @param name Product name.
	 * @return Product entity.
	 * @throws DBException
	 */
	public List<Product> findProduct(String name) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_PRODUCT_BY_NAME)) {
			List<Product> products = new ArrayList<>();
			ProductMapper mapper = new ProductMapper();
			pstmt.setString(1, "%" + name.trim() + "%");
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					products.add(mapper.mapRow(rs));
				}
			}
			return products;
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCTS_BY_NAME, ex);
		}
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
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_ALL_PRODUCT_BY_GENDER_AND_TYPE_PRICE_FROM_TO)) {
			List<Product> products = new ArrayList<>();
			ProductMapper mapper = new ProductMapper();
			int k = 1;
			pstmt.setInt(k++, gender.ordinal());
			pstmt.setInt(k++, type.ordinal());
			pstmt.setInt(k++, from);
			pstmt.setInt(k++, to);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					products.add(mapper.mapRow(rs));
				}
			}
			return products;
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, ex);
		}
	}

	/**
	 * Update product.
	 * 
	 * @param product  product to update.
	 * 
	 * @param quantity is to change quantity
	 * 
	 * @throws DBException
	 */
	public void updateProduct(Product product, int quantity) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT_QUANTITY)) {
			pstmt.setInt(1, product.getQuantity() + quantity);
			pstmt.setLong(2, product.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
		}
	}

	/**
	 * Update product.
	 * 
	 * @param product product to update.
	 * @throws DBException
	 */
	public void updateProduct(Product product) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_PRODUCT)) {
			int k = 1;
			pstmt.setString(k++, product.getName());
			pstmt.setInt(k++, product.getType().ordinal());
			pstmt.setInt(k++, product.getSize());
			pstmt.setInt(k++, product.getGender().ordinal());
			pstmt.setInt(k++, product.getPrice());
			pstmt.setInt(k++, product.getQuantity());
			pstmt.setBlob(k++, product.getImage());
			pstmt.setLong(k++, product.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
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
	public void insertProduct(Product product) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_PRODUCT)) {
			int k = 1;
			pstmt.setString(k++, product.getName());
			pstmt.setInt(k++, product.getType().ordinal());
			pstmt.setInt(k++, product.getSize());
			pstmt.setInt(k++, product.getGender().ordinal());
			pstmt.setInt(k++, product.getPrice());
			pstmt.setInt(k++, product.getQuantity());
			pstmt.setBlob(k++, product.getImage());
			pstmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		}
	}
	
	/**
	 * Delete product from db
	 * 
	 * @param product product to delete
	 * 
	 * @return success
	 * 
	 * @throws DBException
	 */
	public void deleteProduct(Product product) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_DELETE_PRODUCT)) {
			pstmt.setLong(1, product.getId());
			pstmt.execute();
			con.commit();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_UPDATE_PRODUCT, ex);
		}
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
				product.setImage(rs.getBlob(Fields.PRODUCT__IMAGE));
				return product;
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
