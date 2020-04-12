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

import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public final class UserDAO extends DBManager {
	private static final Logger LOG = Logger.getLogger(UserDAO.class);

	// //////////////////////////////////////////////////////////
	// singleton
	// //////////////////////////////////////////////////////////

	private static UserDAO instance;

	public static synchronized UserDAO getInstance() throws DBException {
		if (instance == null) {
			instance = new UserDAO();
		}
		return instance;
	}

	private UserDAO() throws DBException {
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
	private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, 1, NULL)";

	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";

	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";

	private static final String SQL_UPDATE_USER = "UPDATE users SET role=?, city=? WHERE id=?";

	private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";

	/**
	 * Create user.
	 * 
	 * @param user user to update.
	 * @throws DBException
	 */
	public void createUser(User user) throws DBException {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			LOG.trace("connet");
			pstmt = con.prepareStatement(SQL_CREATE_USER);
			int k = 1;
			pstmt.setString(k++, user.getName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setString(k++, user.getPassword());
			LOG.trace("commit");
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
		} finally {
			close(con, pstmt);
		}
	}

	/**
	 * Returns a user with the given login.
	 * 
	 * @param login User login.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUser(String email) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			UserMapper mapper = new UserMapper();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = mapper.mapRow(rs);
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_EMAIL, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Returns a user with the given identifier.
	 * 
	 * @param id User identifier.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUser(long id) throws DBException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			UserMapper mapper = new UserMapper();
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
			pstmt.setLong(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = mapper.mapRow(rs);
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			close(con, pstmt, rs);
		}
		return user;
	}

	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws DBException
	 */
	public void updateUser(User user) throws DBException {
		Connection con = null;
		try {
			con = getConnection();
			updateUser(user, con);
			con.commit();
		} catch (SQLException ex) {
			rollback(con);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		} finally {
			close(con);
		}
	}

	/**
	 * Returns all users.
	 *
	 * @return List of users.
	 */
	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		Statement stm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = getConnection();
			UserMapper mapper = new UserMapper();
			stm = con.createStatement();
			rs = stm.executeQuery(SQL_FIND_ALL_USERS);
			while (rs.next()) {
				users.add(mapper.mapRow(rs));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return users;
	}

	// //////////////////////////////////////////////////////////
	// Entity access methods (for transactions)
	// //////////////////////////////////////////////////////////
	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws SQLException
	 */
	public void updateUser(User user, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(SQL_UPDATE_USER);
			int k = 1;
			pstmt.setInt(k++, user.getRoleId());
			pstmt.setString(k++, user.getCity());
			pstmt.setLong(k, user.getId());
			pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
	}

	/**
	 * Extracts a user from the result set row.
	 */
	private static class UserMapper implements EntityMapper<User> {

		@Override
		public User mapRow(ResultSet rs) {
			try {
				User user = new User();
				user.setId(rs.getLong(Fields.ENTITY__ID));
				user.setName(rs.getString(Fields.USER__NAME));
				user.setEmail(rs.getString(Fields.USER__EMAIL));
				user.setPassword(rs.getString(Fields.USER__PASSWORD));
				user.setRoleId(rs.getInt(Fields.USER__ROLE_ID));
				user.setCity(rs.getString(Fields.USER__CITY));
				return user;
			} catch (SQLException e) {
				throw new IllegalStateException(e);
			}
		}
	}

}
