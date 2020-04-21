package ua.nure.yeshenko.SummaryTask;

public class Path {
	public static final String PAGE_REGISTRATION = "/WEB-INF/jsp/reg.jsp";
	public static final String PAGE_CATALOG = "/WEB-INF/jsp/catalog.jsp";
	public static final String PAGE_ERROR = "/WEB-INF/jsp/error_page.jsp";
	public static final String PAGE_LOGIN = "/login.jsp";
	public static final String PAGE_CART = "/WEB-INF/jsp/cart.jsp";
	public static final String PAGE_LIST_CLIENTS = "/WEB-INF/jsp/admin/list_users.jsp";
	public static final String PAGE_CHECKOUT = "/WEB-INF/jsp/client/checkout.jsp";
	public static final String PAGE_LIST_ORDERS = "/WEB-INF/jsp/list_orders.jsp";
	public static final String PAGE_CHOICE = "/WEB-INF/jsp/choice.jsp";
	public static final String PAGE_UPDATE = "/WEB-INF/jsp/admin/update.jsp";
	
	public static final String COMMAND_LIST_CLIENTS = "/controller?command=listClients";
	public static final String COMMAND_CATALOG = "/controller?command=catalog";
	public static final String COMMAND_CART = "/controller?command=cart";
	public static final String COMMAND_LIST_ORDERS = "/controller?command=allOrders";
	public static final String COMMAND_LOGIN = "/controller?command=login";
}
