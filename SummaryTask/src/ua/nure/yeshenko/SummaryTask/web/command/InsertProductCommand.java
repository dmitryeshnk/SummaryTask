package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Gender;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.Type;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class InsertProductCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7388475283785539097L;
	private static final Logger log = Logger.getLogger(InsertProductCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		Product product = new Product();
		String nameProduct = null;
		int priceProduct;
		int sizeProduct;
		int quantityProduct;
		try {
			nameProduct = request.getParameter("name");
			priceProduct = Integer.valueOf(request.getParameter("price"));
			sizeProduct = Integer.valueOf(request.getParameter("size"));
			quantityProduct = Integer.valueOf(request.getParameter("qt"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR + e);
		}
		log.trace("Get request parameters: name -->" + nameProduct + " price -->" + priceProduct + " size -->"
				+ sizeProduct + "qt -->" + quantityProduct);
		HttpSession session = request.getSession();
		product.setName(nameProduct);
		product.setPrice(priceProduct);
		product.setSize(sizeProduct);
		product.setQuantity(quantityProduct);
		product.setGender((Gender) session.getAttribute("gender"));
		product.setType((Type) session.getAttribute("type"));
		
		ProductDAO.getInstance().insertProduct(product);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__CATALOG);
	}

}
