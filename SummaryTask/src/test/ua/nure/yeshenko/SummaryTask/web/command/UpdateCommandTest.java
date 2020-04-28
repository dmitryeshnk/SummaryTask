package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.UpdateCommand;

class UpdateCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command updateCommand = new UpdateCommand(productDAO);

	@Test
	void shouldThrowExceptionRequestErrorIdIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			updateCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorIdIsString() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn("string");
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			updateCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldReturnPageUpdateParameterNameIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		when(request.getParameter("name")).thenReturn(null);
		
		updateCommand.execute(request, response);
		
		verify(session).setAttribute(eq("mutable"), any());
	}
	
	@Test
	void shouldReturnPageUpdateParameterNameIsEmpty() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		when(request.getParameter("name")).thenReturn("");
		
		updateCommand.execute(request, response);
		
		verify(session).setAttribute(eq("mutable"), any());
	}
	
	@Test
	void shouldThrowExceptionOneOfParameterIsInvalid() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		when(request.getParameter(anyString())).thenReturn("1");
		
		when(request.getPart("image")).thenReturn(mock(Part.class));
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			updateCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldUpdateProduct() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		when(request.getParameter(anyString())).thenReturn("1");
		
		when(request.getPart("image")).thenReturn(mock(Part.class));
		
		
		productDAO.updateProduct(any());
	}
}
