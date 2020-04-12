package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.Status;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class SetOrderStatusCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4996928560908691424L;
	private static final Logger log = Logger.getLogger(SetOrderStatusCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		String status = request.getParameter("status");
		log.trace("Get request patameter: status -->" + status);
		
		OrderDAO manager = OrderDAO.getInstance();
		long orderId;
		try {
			orderId = new Long(request.getParameter("id"));
		} catch(Exception ex) {
			log.error(Messages.ERR_REQUEST_ERROR + ex);
			throw new AppException(Messages.ERR_REQUEST_ERROR + ex);
		}
		
		log.trace("Get request parameter: id -->" + orderId);
		Order order = new Order();
		order.setId(orderId);
		
		order = manager.findOrder(order);
		log.trace("Find order in DB --> " + order);
		
		if(status.equals(Status.PAID.getName())) {
			manager.updateOrder(order, true);
		} else if(status.equals(Status.CANCELED.getName())) {
			manager.updateOrder(order, false);
		} else {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__LIST_ORDERS);
	}

}
