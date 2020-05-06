package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

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
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class OrderToCartCommand extends Command {
	private static final Logger log = Logger.getLogger(OrderToCartCommand.class);
	private ProductDAO productDAO;

	public OrderToCartCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();

		if (session.getAttribute("userRole") == Role.BLOCKED) {
			log.error(Messages.ERR_USER_IS_BLOCKED);
			throw new AppException(Messages.ERR_USER_IS_BLOCKED);
		}

		CartBean cart = CartBean.get(session);
		int productId;
		try {
			productId = Integer.valueOf(request.getParameter("id"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}

		log.trace("Request parameter: id --> " + productId);

		Product product = productDAO.findProduct(productId);
		log.trace("Find product in DB -->" + product);
		if(product == null) {
			log.error(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
		}
		if(product.getQuantity() <= 0) {
			return createRedirectResult(Path.COMMAND_CATALOG);
		}
		product.setQuantity(product.getQuantity() - 1);
		cart.addItem(product);
		productDAO.updateProduct(product, product.getQuantity());
		log.trace("Update product in DB (change quantity)");

		session.setAttribute("cart", cart);
		log.trace("Set the session attribute: cart --> " + cart);
		session.setAttribute("inside", cart.getCart().size());
		session.setAttribute("isEmptyCart", false);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_CATALOG);
	}

}
