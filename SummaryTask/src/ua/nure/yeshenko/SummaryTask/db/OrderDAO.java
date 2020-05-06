package ua.nure.yeshenko.SummaryTask.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Status;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class OrderDAO {
	private static OrderDAO instance;
	private OrderDAO() {
		
	}
	
	public static OrderDAO getInstance() {
		if(instance == null) {
			instance = new OrderDAO();
		}
		return instance;
	}
	
	private static final String SQL_FIND_ORDER_BY_ID = "SELECT * FROM orders WHERE id=?";

	private static final String SQL_FIND_ALL_ORDERS = "SELECT * FROM orders ORDER BY id DESC";

	private static final String SQL_FIND_ORDERS_ON_USER = "SELECT * FROM `orders` WHERE user_id = ? ORDER BY id DESC";

	private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE orders SET status=? WHERE id=?";

	private static final String SQL_INSERT_ORDER = "INSERT INTO orders VALUES(DEFAULT, ?, 0, ?, ?)";

	public Order findOrder(Order order) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_ORDER_BY_ID)) {
			OrderMapper mapper = new OrderMapper();

			pstmt.setLong(1, order.getId());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapper.mapRow(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDER_BY_ID, ex);
		}
		return null;
	}

	/**
	 * Returns all orders.
	 *
	 * @return List of order entities.
	 */
	public List<Order> findAllOrders() throws DBException {
		List<Order> orders = new ArrayList<>();
		try (Connection con = DBManager.getConnection(); Statement stm = con.createStatement()) {
			OrderMapper mapper = new OrderMapper();
			try (ResultSet rs = stm.executeQuery(SQL_FIND_ALL_ORDERS)) {
				while (rs.next()) {
					orders.add(mapper.mapRow(rs));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return orders;
	}

	/**
	 * Returns orders of the given user
	 *
	 * @param user User entity.
	 * 
	 * @return List of order entities.
	 */
	public List<Order> findAllOrders(User user) throws DBException {
		List<Order> orders = new ArrayList<>();
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_ORDERS_ON_USER)) {
			OrderMapper mapper = new OrderMapper();
			pstmt.setLong(1, user.getId());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					orders.add(mapper.mapRow(rs));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return orders;
	}

	/**
	 * Returns orders of the given user
	 *
	 * @param order Order entity.
	 * 
	 * @param bool  true to paid, false to canceled
	 * 
	 * @return Order entity
	 * 
	 * @throws DBException
	 */

	public void updateOrder(Order order) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_ORDER_STATUS)) {
			int k = 1;
			pstmt.setInt(k++, order.getStatus().ordinal());
			pstmt.setLong(k, order.getId());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 
	 * @param order Order entity
	 * 
	 * @param con   Connection
	 * 
	 * @return operation success
	 * @throws DBException 
	 * 
	 * @throws SQLException
	 */
	public boolean insertOrder(Order order, Connection con) {
		try (PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {
			int k = 1;
			pstmt.setInt(k++, order.getCost());
			pstmt.setString(k++, order.getProductsId());
			pstmt.setLong(k++, order.getUser_id());
			pstmt.executeUpdate();
		}  catch (SQLException e) {
			return false;
		}
		return true;
	}

	/**
	 * Extracts order from the result set row.
	 */
	private static class OrderMapper implements EntityMapper<Order> {

		@Override
		public Order mapRow(ResultSet rs) {
			try {
				Order order = new Order();
				order.setId(rs.getLong(Fields.ENTITY__ID));
				order.setUser_id(rs.getLong(Fields.ORDER__USER_ID));
				order.setCost(rs.getInt(Fields.ORDER__COST));
				order.setStatus(Status.values()[rs.getInt(Fields.ORDER__STATUS)]);
				order.setProductsId(rs.getString(Fields.ORDER__PRODUCTS_ID));
				return order;
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
