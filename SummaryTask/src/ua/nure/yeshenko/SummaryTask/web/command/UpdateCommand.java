package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Gender;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.Role;
import ua.nure.yeshenko.SummaryTask.db.Type;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class UpdateCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6319271394298142494L;
	private static final Logger log = Logger.getLogger(UpdateCommand.class);
	
	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		if(session.getAttribute("userRole") != Role.ADMIN) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		long productId;
		try {
			productId = Long.valueOf(request.getParameter("id"));
		}catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR + e);
		}
		ProductDAO manager = ProductDAO.getInstance();
		
		Product product = manager.findProduct(productId);
		log.trace("Find product in DB -->" + product);
		
		if (request.getParameter("name") == null || request.getParameter("name").isEmpty()) {
			session.setAttribute("mutable", product);
			return createRedirectResult("/update.jsp");
		}
		
		try {
			product.setName(request.getParameter("name"));
			product.setPrice(Integer.valueOf(request.getParameter("price")));
			product.setSize(Integer.valueOf(request.getParameter("size")));
			product.setQuantity(Integer.valueOf(request.getParameter("quantity")));
			product.setGender(Gender.values()[Integer.valueOf(request.getParameter("gender"))]);
			product.setType(Type.values()[Integer.valueOf(request.getParameter("type"))]);
		} catch (Exception e) {
			log.error(Messages.ERR_CANNOT_UPDATE_PRODUCT);
			throw new AppException(Messages.ERR_CANNOT_UPDATE_PRODUCT);
		}

		manager.updateProduct(product);
		log.trace("Update product in DB -->" + product);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__CATALOG);
	}

}
