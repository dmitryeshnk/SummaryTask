package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class ListOrderCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5329998116439437145L;
	private static final Logger log = Logger.getLogger(ListOrderCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		
		List<Order> orders;
		OrderDAO manager = OrderDAO.getInstance();
		Role userRole = (Role) session.getAttribute("userRole");
		log.trace("Get the session attribute: userRole --> " + userRole);
		if(userRole == Role.ADMIN) {
			orders = manager.findAllOrders();
		} else {
			User user = (User) session.getAttribute("user");
			log.trace("Get the session attribute: user --> " + user);
			orders = manager.findAllOrders(user);
		}
		session.setAttribute("isOrderEmpty", false);
		if(orders == null) {
			session.setAttribute("isOrderEmpty", true);
			log.trace("Set the session attribute: isOrderEmpty --> " + true);
			return createForwardResult(Path.PAGE__LIST_ORDERS);
		}
		log.trace("Set the session attribute: isOrderEmpty --> " + false);
		session.setAttribute("orders", orders);
		log.trace("Set the session attribute: orders --> " + orders);
		log.debug("Command finish");
		return createForwardResult(Path.PAGE__LIST_ORDERS);
	}

}
