package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class DeleteFromCartCommand extends Command {
	private static final Logger log = Logger.getLogger(DeleteFromCartCommand.class);
	private ProductDAO productDAO;

	public DeleteFromCartCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		CartBean cart = CartBean.get(session);
		if (cart.getCart().isEmpty()) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		int productId;
		try {
			productId = Integer.valueOf((request.getParameter("id")));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR + e);
		}
		log.trace("Request parameter: id --> " + productId);

		Product product = productDAO.findProduct(productId);
		log.trace("Find product in DB -->" + product);
		productDAO.updateProduct(product, cart.getCart().get(product));
		cart.deleteItem(product, true);
		log.trace("Update product in DB (change quantity)");

		session.setAttribute("cart", cart);
		log.trace("Set the session attribute: cart --> " + cart.getCart());
		session.setAttribute("inside", cart.getCart().size());
		log.debug("Command finish");
		return createForwardResult(Path.PAGE_CART);
	}

}
