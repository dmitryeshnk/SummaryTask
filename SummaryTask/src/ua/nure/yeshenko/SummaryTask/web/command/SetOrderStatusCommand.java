package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Status;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class SetOrderStatusCommand extends Command {
	private static final Logger log = Logger.getLogger(SetOrderStatusCommand.class);
	private OrderDAO orderDAO;

	public SetOrderStatusCommand(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		String status = request.getParameter("status");
		log.trace("Get request patameter: status -->" + status);

		if (status == null || status.isEmpty()) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}

		long orderId;
		try {
			orderId = new Long(request.getParameter("id"));
			log.trace("Get request parameter: id -->" + orderId);
		} catch (Exception ex) {
			log.error(Messages.ERR_REQUEST_ERROR + ex);
			throw new AppException(Messages.ERR_REQUEST_ERROR, ex);
		}
		Order order = new Order();
		order.setId(orderId);
		order = orderDAO.findOrder(order);
		log.trace("Find order in DB --> " + order);
		if(order == null) {
			log.error(Messages.ERR_CANNOT_OBTAIN_ORDER);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_ORDER);
		}
		try {
			order.setStatus(Status.valueOf(status.toUpperCase()));
			orderDAO.updateOrder(order);
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}

		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_LIST_ORDERS);
	}

}
