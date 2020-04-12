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

import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class OrderDAO extends DBManager {
	private static final Logger LOG = Logger.getLogger(OrderDAO.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static OrderDAO instance;

	public static synchronized OrderDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new OrderDAO();
		}
		return instance;
	}

	private OrderDAO() throws DBException {
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
	private static final String SQL_FIND_ORDER_BY_ID = "SELECT * FROM orders WHERE id=?";
	
	private static final String SQL_FIND_ALL_ORDERS = "SELECT * FROM orders ORDER BY id";

	private static final String SQL_FIND_ORDERS_ON_USER = "SELECT * FROM `orders` WHERE user_id = ?";

	private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE orders SET status=? WHERE id=?";

	private static final String SQL_INSERT_ORDER = "INSERT INTO orders VALUES(DEFAULT, ?, 0, ?, ?)";

	public Order findOrder(Order order) throws DBException {
		Order result = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			OrderMapper mapper = new OrderMapper();
			pstmt = con.prepareStatement(SQL_FIND_ORDER_BY_ID);
			pstmt.setLong(1, order.getId());
			rs = pstmt.executeQuery();
			if(rs.next()) {
				result = mapper.mapRow(rs);
			}
		} catch(SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_ORDER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return result;
	}
	
	/**
	 * Returns all orders.
	 *
	 * @return List of order entities.
	 */
	public List<Order> findAllOrders() throws DBException {
		List<Order> orders = new ArrayList<>();
		Statement stm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			OrderMapper mapper = new OrderMapper();
			stm = con.createStatement();
			rs = stm.executeQuery(SQL_FIND_ALL_ORDERS);
			while (rs.next()) {
				orders.add(mapper.mapRow(rs));
			}
			rs.close();
			stm.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBManager.close(con);
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			OrderMapper mapper = new OrderMapper();
			pstmt = con.prepareStatement(SQL_FIND_ORDERS_ON_USER);
			pstmt.setLong(1, user.getId());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				orders.add(mapper.mapRow(rs));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBManager.close(con);
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

	public void updateOrder(Order order, boolean bool) throws DBException {
		PreparedStatement pstmt = null;
		Connection con = null;
		try {
			con = getConnection();
			pstmt = con.prepareStatement(SQL_UPDATE_ORDER_STATUS);
			int k = 1;
			if (bool) {
				pstmt.setInt(k++, Status.PAID.ordinal());
				order.setStatus(Status.PAID);
			} else {
				pstmt.setInt(k++, Status.CANCELED.ordinal());
				order.setStatus(Status.CANCELED);
			}
			pstmt.setLong(k, order.getId());
			pstmt.executeUpdate();
			pstmt.close();
			con.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBManager.close(con);
		}
	}

	/**
	 * 
	 * @param order Order entity
	 * 
	 * @param con   Connection
	 * 
	 * @return operation success
	 * 
	 * @throws SQLException
	 */
	public boolean insertOrder(Order order, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		pstmt = con.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
		int k = 1;
		pstmt.setInt(k++, order.getCost());
		pstmt.setString(k++, order.getProductsId());
		pstmt.setLong(k++, order.getUser_id());
		pstmt.executeUpdate();
		
		close(pstmt);
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
