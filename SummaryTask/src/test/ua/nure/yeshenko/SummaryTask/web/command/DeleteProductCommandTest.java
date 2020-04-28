package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.DeleteProductCommand;

class DeleteProductCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command deleteProductCommand = new DeleteProductCommand(productDAO);

	@Test
	void shouldThrowExceptionRequestErrorParameterIdDidNotFit() {
		when(request.getParameter("id")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteProductCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionCannotObtainProduct() {
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteProductCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
	}

	@Test
	void shouldDeleteTheProductFromDataBase() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		
		deleteProductCommand.execute(request, response);
		
		verify(productDAO).deleteProduct(any());
	}

}
