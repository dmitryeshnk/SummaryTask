package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;
import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.DBManager;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Status;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class ConfirmCommand extends Command {
	private static final Logger log = Logger.getLogger(ConfirmCommand.class);
	private OrderDAO orderDAO;
	private UserDAO userDAO;
	
	public ConfirmCommand(OrderDAO orderDAO, UserDAO userDAO) {
		this.orderDAO = orderDAO;
		this.userDAO = userDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();

		CartBean cart = CartBean.get(session);

		Order order = new Order();
		int totalCost;
		try {
			totalCost = Integer.valueOf(request.getParameter("total"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}
		 
		log.trace("Request parameter: total --> " + totalCost);

		order.setCost(totalCost);
		User user = (User) session.getAttribute("user");
		if(user == null) {
			return createForwardResult(Path.PAGE_LOGIN);
		}
		String city = request.getParameter("city");
		log.trace("Request parameter: city --> " + city);
		if (city == null || city.isEmpty()) {
			session.setAttribute("isEmptyCity", true);
			log.trace("Set the session attribute: isEmptyCity --> " + true);
			return createForwardResult(Path.PAGE_CHECKOUT);
		}
		user.setCity(city);
		order.setUser_id(user.getId());
		order.setStatus(Status.REGISTERED);
		order.setProductsId(cart.toString());
		Connection con = null;
		Savepoint save = null;
		try {
			con = DBManager.getConnection();
			save = con.setSavepoint();
			orderDAO.insertOrder(order, con);
			userDAO.updateUser(user, con);
			con.commit();
			con.close();
		} catch (SQLException ex) {
			try {
				con.rollback(save);
			} catch (SQLException e) {
				log.error(Messages.ERR_CANNOT_ROLLBACK + e);
				throw new DBException(Messages.ERR_CANNOT_ROLLBACK, e);
			}
			log.error(Messages.ERR_CANNOT_COMPLETE_TRANSACTION + ex);
			ex.printStackTrace();
			throw new DBException(Messages.ERR_CANNOT_COMPLETE_TRANSACTION, ex);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				log.error(Messages.ERR_CANNOT_CLOSE_CONNECTION + e);
				throw new DBException(Messages.ERR_CANNOT_CLOSE_CONNECTION, e);
			}
		}
		cart.clear();
		session.setAttribute("cart", cart);
		log.trace("Set the session attribute: cart --> " + cart.getCart());
		session.setAttribute("inside", cart.getCart().size());
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_LIST_ORDERS);
	}

}
