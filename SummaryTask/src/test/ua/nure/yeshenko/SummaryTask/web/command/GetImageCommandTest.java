package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Blob;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.GetImageCommand;

class GetImageCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command getImageCommand = new GetImageCommand(productDAO);

	@Test
	void shouldThrowNumberFormatExceptionIdDidNotFit()  {
		when(request.getParameter("id")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			getImageCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	@Test
	void shouldThrowSQLExceptionDidNotFindProduct()  {
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			getImageCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_OBTAIN_PRODUCT);
	}
	
	@Test
	void shouldSetContentOnResponse() throws Exception{
		when(request.getParameter("id")).thenReturn("1");
		Product product = new Product();
		product.setImage(mock(Blob.class));
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		
		getImageCommand.execute(request, response);
		
		verify(response).setContentType("image/jpg");
		verify(response).getOutputStream().write(any());
	}

}
