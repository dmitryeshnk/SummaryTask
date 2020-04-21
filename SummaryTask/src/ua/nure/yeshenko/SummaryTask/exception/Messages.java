package ua.nure.yeshenko.SummaryTask.exception;
/**
 * Holder for messages of exceptions.
 * 
 * @author Dmytro_Kolesnikov
 *
 */
public class Messages {

	private Messages() {
		// no op
	}
	
	public static final String ERR_CANNOT_UPDATE_PRODUCT = "Cannot update the product";

	public static final String ERR_CANNOT_OBTAIN_CONNECTION = "Cannot obtain a connection from the pool";

	public static final String ERR_USER_IS_BLOCKED = "User is blocked";

	public static final String ERR_DELETING_FROM_CART = "Error deleting from cart";

	public static final String ERR_CANNOT_ROLLBACK = "Cannot rollback";

	public static final String ERR_CANNOT_COMPLETE_TRANSACTION = "Cannot complete transaction";

	public static final String ERR_CANNOT_OBTAIN_ORDER_BY_ID = "Cannot obtain order by id";

	public static final String ERR_CANNOT_OBTAIN_USER_BY_EMAIL = "Cannot obtain user by email";

	public static final String ERR_REQUEST_ERROR = "Request error";

	public static final String ERR_CANNOT_OBTAIN_PRODUCT_BY_ID = "Cannot obtain products by id";
	
	public static final String ERR_CANNOT_OBTAIN_PRODUCT = "Cannot obtain products";

	public static final String ERR_CANNOT_OBTAIN_USER_BY_ID = "Cannot obtain a user by its id";

	public static final String ERR_CANNOT_OBTAIN_PRODUCTS_BY_NAME = "Cannot obtain products by name";

	public static final String ERR_CANNOT_UPDATE_USER = "Cannot update a user";
	
	public static final String ERR_CANNOT_CREATE_USER = "Cannot create a user";

	public static final String ERR_CANNOT_CLOSE_CONNECTION = "Cannot close a connection";

	public static final String ERR_CANNOT_CLOSE_RESULTSET = "Cannot close a result set";

	public static final String ERR_CANNOT_CLOSE_STATEMENT = "Cannot close a statement";

	public static final String ERR_CANNOT_OBTAIN_DATA_SOURCE = "Cannot obtain the data source";
	
	public static final String ERR_CANNOT_INSERT_ORDER = "Cannot insert the order";
	
	public static final String ERR_CANNOT_FIND_SORTER = "Cannot find sorter value";

	public static final String ERR_CANNOT_INSERT_PRODUCT = "Cannot insert the product";
	
}