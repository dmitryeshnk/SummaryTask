package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.SearchCommand;

class SearchCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command searchCommand = new SearchCommand(productDAO);
	
	@Test
	void shouldThrowExceptionRequestErrorParameterNameIsNull() {
		when(request.getParameter("name")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			searchCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorParameterNameIsEmpty() {
		when(request.getParameter("name")).thenReturn("");
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			searchCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldSearchProductsListFromDataBase() throws Exception{
		when(request.getParameter("name")).thenReturn("string");
		when(productDAO.findProduct(anyString())).thenReturn(Collections.emptyList());
		when(request.getSession()).thenReturn(session);
		
		searchCommand.execute(request, response);
		
		verify(session, times(2)).setAttribute(any(), any());
	}

}
