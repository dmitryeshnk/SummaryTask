package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Gender;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.Type;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;
import ua.nure.yeshenko.SummaryTask.sort.CompareBy;

public class CatalogCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729532489370340085L;
	private static final Logger log = Logger.getLogger(CatalogCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		Gender gender = (Gender) session.getAttribute("gender");
		if (gender == null) {
			return createForwardResult(Path.PAGE__CHOICE);
		}

		Type type = (Type) session.getAttribute("type");
		if (type == null) {
			return createForwardResult(Path.PAGE__CHOICE);
		}
		List<Product> products;
		if ((request.getParameter("from") == null && request.getParameter("to") == null) ||
				 (request.getParameter("from").isEmpty() && request.getParameter("to").isEmpty())) {
			products = ProductDAO.getInstance().findAllProduct(gender, type);
			log.trace("Selected: gender->" + gender + " type->" + type);
		} else {
			int fromPrice;
			int toPrice;
			try {
				fromPrice = Integer.valueOf(request.getParameter("from"));
			} catch (Exception e) {
				fromPrice = 1;
			}
			log.trace("Get request parameter: from --> " + fromPrice);
			try {
				toPrice = Integer.valueOf(request.getParameter("to"));
			} catch (Exception e) {
				toPrice = Integer.MAX_VALUE;
			}
			log.trace("Get request parameter: to --> " + fromPrice);
			products = ProductDAO.getInstance().findAllProduct(fromPrice, toPrice, gender, type);
			log.trace("Selected: gender->" + gender + " type->" + type + " from->" + fromPrice + " to->" + toPrice);
		}
		String forward = Path.PAGE_ERROR;
		if (products == null) {
			session.setAttribute("isNullProduct", true);
			return createForwardResult(forward);
		}
		String value = request.getParameter("select");
		log.trace("Selected sorter->" + value);
		Collections.sort(products, new CompareBy(value));
		session.setAttribute("products", products);
		log.trace("Set the session attribute: products --> " + products);
		session.setAttribute("selected", value);
		log.trace("Set the session attribute: selected --> " + value);
		forward = Path.PAGE_CATALOG;
		return createForwardResult(forward);
	}
}
