package ua.nure.yeshenko.SummaryTask.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class UserDAO {
	private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, 1, NULL)";

	private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM users WHERE email=?";

	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";

	private static final String SQL_UPDATE_USER = "UPDATE users SET role=?, city=? WHERE id=?";

	private static final String SQL_FIND_ALL_USERS = "SELECT * FROM users";

	private static final String SQL_DELETE_USER_BY_EMAIL = "DELETE FROM users WHERE email = ?";

	/**
	 * Create user.
	 * 
	 * @param user user to update.
	 * @throws DBException
	 */
	public void createUser(User user) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_CREATE_USER)) {
			int k = 1;
			pstmt.setString(k++, user.getName());
			pstmt.setString(k++, user.getEmail());
			pstmt.setString(k++, user.getPassword());
			pstmt.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_CREATE_USER, ex);
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
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_USER_BY_EMAIL)) {
			UserMapper mapper = new UserMapper();
			pstmt.setString(1, email);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapper.mapRow(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_EMAIL, ex);
		}
		return null;
	}

	/**
	 * Returns a user with the given identifier.
	 * 
	 * @param id User identifier.
	 * @return User entity.
	 * @throws DBException
	 */
	public User findUser(long id) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID)) {
			UserMapper mapper = new UserMapper();
			pstmt.setLong(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return mapper.mapRow(rs);
				}
			}
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		}
		return null;
	}

	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws DBException
	 */
	public void updateUser(User user) throws DBException {
		try (Connection con = DBManager.getConnection()) {
			updateUser(user, con);
			con.commit();
		} catch (SQLException ex) {
			throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, ex);
		}
	}

	/**
	 * Returns all users.
	 *
	 * @return List of users.
	 */
	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		try (Connection con = DBManager.getConnection(); Statement stm = con.createStatement()) {
			UserMapper mapper = new UserMapper();
			try (ResultSet rs = stm.executeQuery(SQL_FIND_ALL_USERS)) {
				while (rs.next()) {
					users.add(mapper.mapRow(rs));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return users;
	}

	/**
	 * Update user.
	 * 
	 * @param user user to update.
	 * @throws SQLException
	 */
	public void updateUser(User user, Connection con) throws DBException {
		try (PreparedStatement pstmt = con.prepareStatement(SQL_UPDATE_USER)) {
			int k = 1;
			pstmt.setInt(k++, user.getRoleId());
			pstmt.setString(k++, user.getCity());
			pstmt.setLong(k, user.getId());
			pstmt.execute();
		} catch (SQLException e) {
			DBManager.rollback(con);
			throw new DBException(Messages.ERR_CANNOT_COMPLETE_TRANSACTION, e);
		}
	}

	public void deleteUser(String email) throws DBException {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pstmt = con.prepareStatement(SQL_DELETE_USER_BY_EMAIL)) {
			pstmt.setString(1, email);
			pstmt.execute();
		} catch (SQLException e) {
			throw new DBException(Messages.ERR_CANNOT_DELETE_USER, e);
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
