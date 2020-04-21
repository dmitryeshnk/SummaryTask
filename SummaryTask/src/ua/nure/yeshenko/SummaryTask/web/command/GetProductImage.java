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

public class GetProductImage extends Command {
	private ProductDAO productDAO;
	
	public GetProductImage(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		int id;
		try {
			id = Integer.valueOf(request.getParameter("id"));
		}catch (Exception e) {
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}
		Product product = productDAO.findProduct(id);
		response.setContentType("image/jpg");
//		try {
//			Blob image = product.getImage();
//			response.getOutputStream().write(image.getBytes(0,(int) image.length()));
//		} catch (SQLException e) {
//			throw new AppException(Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID, e);
//		} catch (IOException e) {
//			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
//		}
		return null;
	}

}
