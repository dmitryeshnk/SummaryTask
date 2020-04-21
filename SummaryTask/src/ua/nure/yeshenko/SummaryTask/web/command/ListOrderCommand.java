package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class ListOrderCommand extends Command {
	private static final Logger log = Logger.getLogger(ListOrderCommand.class);
	private OrderDAO orderDAO;
	private ProductDAO productDAO;

	public ListOrderCommand(OrderDAO orderDAO, ProductDAO productDAO) {
		this.orderDAO = orderDAO;
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();

		List<Order> orders;
		Role userRole = (Role) session.getAttribute("userRole");
		log.trace("Get the session attribute: userRole --> " + userRole);
		if (userRole == Role.ADMIN) {
			orders = orderDAO.findAllOrders();
		} else {
			User user = (User) session.getAttribute("user");
			log.trace("Get the session attribute: user --> " + user);
			orders = orderDAO.findAllOrders(user);
		}
		session.setAttribute("isOrderEmpty", false);
		if (orders == null) {
			session.setAttribute("isOrderEmpty", true);
			log.trace("Set the session attribute: isOrderEmpty --> " + true);
			return createForwardResult(Path.PAGE_LIST_ORDERS);
		}
		log.trace("Set the session attribute: isOrderEmpty --> " + false);
		Map<Long, List<Product>> products = new ConcurrentHashMap<>();
		
		for(Order order : orders) {
			String[] productsId = order.getProductsId().split("&");
			List<Product> productsInOrder = new ArrayList<>();
			for(String product: productsId) {
				productsInOrder.add(productDAO.findProduct(Integer.valueOf(product)));
			}
			products.put(order.getId(), productsInOrder);
		}
		session.setAttribute("productsId", products);
		session.setAttribute("orders", orders);
		log.trace("Set the session attribute: orders --> " + orders);
		log.debug("Command finish");
		return createForwardResult(Path.PAGE_LIST_ORDERS);
	}

}
