package ua.nure.yeshenko.SummaryTask.db;

/**
 * Holder for fields names of DB tables and beans.
 * 
 * @author D.Kolesnikov
 * 
 */
public class Fields {
	// entities
	public static final String ENTITY__ID = "id";

	public static final String USER__NAME = "name";
	public static final String USER__PASSWORD = "password";
	public static final String USER__EMAIL = "email";
	public static final String USER__ROLE_ID = "role";
	public static final String USER__CITY = "city";

	public static final String ORDER__COST = "cost";
	public static final String ORDER__USER_ID = "user_id";
	public static final String ORDER__STATUS = "status";
	public static final String ORDER__PRODUCTS_ID = "list_products";
	
	
	public static final String PRODUCT__NAME = "name";
	public static final String PRODUCT__TYPE = "type";
	public static final String PRODUCT__SIZE = "size";
	public static final String PRODUCT__GENDER = "gender";
	public static final String PRODUCT__PRICE = "price";
	public static final String PRODUCT__QUANTITY = "quantity";
	public static final String PRODUCT__IMAGE = "image";
}
