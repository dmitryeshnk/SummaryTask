package ua.nure.yeshenko.SummaryTask.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

public class DBManager {

	private static final Logger LOG = Logger.getLogger(DBManager.class);
	private static DataSource dataSource;
	/**
	 * Returns a DB connection from the Pool Connections. Before using this
	 * method you must configure the Date Source and the Connections Pool in
	 * your WEB_APP_ROOT/META-INF/context.xml file.
	 * 
	 * @return DB connection.
	 */
	public static Connection getConnection() throws DBException {
		Connection con = null;
		try {
			con = dataSource.getConnection();
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
	}
	
	public static void setDataSource(DataSource dataSource) {
		DBManager.dataSource = dataSource;
	}
	
	/**
	 * Rollbacks a connection.
	 * 
	 * @param con
	 *            Connection to be rollbacked.
	 */
	static void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException ex) {
				LOG.error("Cannot rollback transaction", ex);
			}
		}
	}
}
