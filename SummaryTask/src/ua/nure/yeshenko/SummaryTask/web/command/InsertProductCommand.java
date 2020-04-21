package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;
import ua.nure.yeshenko.SummaryTask.util.FileConverter;

public class InsertProductCommand extends Command {
	private static final Logger log = Logger.getLogger(InsertProductCommand.class);
	private ProductDAO productDAO;

	public InsertProductCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command start");
		Product product = new Product();
		try {
			product.setName(request.getParameter("name"));
			product.setPrice(Integer.valueOf(request.getParameter("price")));
			product.setSize(Integer.valueOf(request.getParameter("size")));
			product.setQuantity(Integer.valueOf(request.getParameter("quantity")));
			product.setGender(Gender.values()[Integer.valueOf(request.getParameter("gender"))]);
			product.setType(Type.values()[Integer.valueOf(request.getParameter("type"))]);
			Part file = request.getPart("image");
			product.setImage(FileConverter.convert(file.getInputStream()));
		} catch (Exception e) {
			log.error(Messages.ERR_CANNOT_INSERT_PRODUCT);
			e.printStackTrace();
			throw new AppException(Messages.ERR_CANNOT_UPDATE_PRODUCT, e);
		}
		
		productDAO.insertProduct(product);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_CATALOG);
	}

}
