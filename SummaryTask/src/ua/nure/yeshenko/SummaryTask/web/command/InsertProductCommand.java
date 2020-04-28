package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

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
		HttpSession session = request.getSession();
		Product product = new Product();
		try {
			product.setName(request.getParameter("name"));
			product.setPrice(Integer.valueOf(request.getParameter("price")));
			product.setSize(Integer.valueOf(request.getParameter("size")));
			product.setQuantity(Integer.valueOf(request.getParameter("quantity")));
			product.setGender((Gender) session.getAttribute("gender"));
			product.setType((Type) session.getAttribute("type"));
			Part file = request.getPart("image");
			byte[] bytesArray = new byte[(int)file.getSize()];
			try(InputStream fis = file.getInputStream()) {
				fis.read(bytesArray);
			}
			product.setImage(new SerialBlob(bytesArray));
		} catch (Exception e) {
			log.error(Messages.ERR_CANNOT_INSERT_PRODUCT);
			throw new AppException(Messages.ERR_CANNOT_INSERT_PRODUCT, e);
		}
		
		productDAO.insertProduct(product);
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_CATALOG);
	}

}
