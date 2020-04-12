package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.Role;
import ua.nure.yeshenko.SummaryTask.db.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class OrderToCartCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7637440369440512637L;
	private static final Logger log = Logger.getLogger(OrderToCartCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		ProductDAO manager = ProductDAO.getInstance();
		
		if(session.getAttribute("userRole") == Role.BLOCKED) {
			log.error(Messages.ERR_USER_IS_BLOCKED);
			throw new AppException(Messages.ERR_USER_IS_BLOCKED);
		}
		
		CartBean cart = CartBean.get(session);
		int productId;
		try {
			productId = Integer.valueOf(request.getParameter("id"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR + e);
		}
		
		log.trace("Request parameter: id --> " + productId);
		
		Product product = manager.findProduct(productId);
		log.trace("Find product in DB -->" + product);
		cart.addItem(product);
		manager.updateProduct(product, true);
		log.trace("Update product in DB (change quantity)");

		session.setAttribute("cart", cart);
		log.trace("Set the session attribute: cart --> " + cart);
		session.setAttribute("inside", cart.getCart().size());
		session.setAttribute("isEmptyCart", false);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__CATALOG);
	}

}
