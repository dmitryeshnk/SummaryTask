package ua.nure.yeshenko.SummaryTask.exception;

/**
 * An exception that provides information on a database access error.
 * 
 * @author D.Yeshenko
 * 
 */
public class DBException extends AppException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5041580558559762313L;

	public DBException() {
		super();
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

}
