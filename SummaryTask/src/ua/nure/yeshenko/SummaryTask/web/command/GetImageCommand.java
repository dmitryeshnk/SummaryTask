package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class GetImageCommand extends Command{
	
	private ProductDAO productDAO;
	
	
	public GetImageCommand(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		try {
			int id = Integer.valueOf(request.getParameter("id"));
			Product product = productDAO.findProduct(id);
			Blob blob = product.getImage();
			response.setContentType("image/jpg");
			response.getOutputStream().write(blob.getBytes(1, (int)blob.length()));
		} catch(NumberFormatException e) {
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		} catch (SQLException e) {
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_PRODUCT, e);
		}
		return null;
	}

}
