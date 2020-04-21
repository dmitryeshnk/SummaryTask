package ua.nure.yeshenko.SummaryTask.exception;

/**
 * An exception that provides information on an application error.
 * 
 * @author D.Yeshenko
 * 
 */
public class AppException extends RuntimeException {
	private static final long serialVersionUID = -8337907115632585909L;

	public AppException() {
		super();
	}

	public AppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppException(String message) {
		super(message);
	}

}

