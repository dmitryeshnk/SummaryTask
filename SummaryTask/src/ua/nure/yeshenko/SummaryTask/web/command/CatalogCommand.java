package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class CatalogCommand extends Command {
	private static final Logger log = Logger.getLogger(CatalogCommand.class);
	private ProductDAO productDAO;

	public CatalogCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		Gender gender = (Gender) session.getAttribute("gender");
		if (gender == null) {
			return createForwardResult(Path.PAGE_CHOICE);
		}

		Type type = (Type) session.getAttribute("type");
		if (type == null) {
			return createForwardResult(Path.PAGE_CHOICE);
		}
		log.trace("Selected: gender->" + gender + " type->" + type);

		List<Product> products = productDAO.findAllProduct(1, Integer.MAX_VALUE, gender, type);
		String forward = Path.PAGE_ERROR;
		if (products == null|| products.isEmpty()) {
			session.setAttribute("isNullProduct", true);
			return createForwardResult(forward);
		}
		session.setAttribute("products", products);
		log.trace("Set the session attribute: products --> " + products);
		forward = Path.PAGE_CATALOG;
		return createForwardResult(forward);
	}
}
