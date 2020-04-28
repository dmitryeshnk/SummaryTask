package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class DeleteProductCommand extends Command {
	private ProductDAO productDAO;
	private static final Logger log = Logger.getLogger(DeleteProductCommand.class);
	
	public DeleteProductCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		long id;
		try {
			id = Long.valueOf(request.getParameter("id"));
		} catch (NumberFormatException e) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}
		
		Product product = productDAO.findProduct(id);
		if(product == null) {
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
		}
		log.debug("Get product from db with id --> "+product.getId());
		productDAO.deleteProduct(product);
		log.debug("Product deleted");
		
		
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_CATALOG);
	}

}
