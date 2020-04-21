package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;
import java.util.Collections;
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
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;
import ua.nure.yeshenko.SummaryTask.sort.CompareBy;
import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

public class SortCommand extends Command {
	private static final Logger log = Logger.getLogger(SortCommand.class);
	private ProductDAO productDAO;
	
	public SortCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		Gender gender = (Gender) session.getAttribute("gender");
		Type type = (Type) session.getAttribute("type");
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
		List<Product> products = productDAO.findAllProduct(fromPrice, toPrice, gender, type);
		
		String sorter = request.getParameter("select");
		log.trace("Selected sorter->" + sorter);
		CompareBy compare;
		try {
			compare = (CompareBy) Class.forName("ua.nure.yeshenko.SummaryTask.sort." + sorter.trim()).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new AppException(Messages.ERR_CANNOT_FIND_SORTER, e);
		}
		Collections.sort(products, compare);
		session.setAttribute("products", products);
		log.debug("Command finish");
		return createForwardResult(Path.PAGE_CATALOG);
	}

}
