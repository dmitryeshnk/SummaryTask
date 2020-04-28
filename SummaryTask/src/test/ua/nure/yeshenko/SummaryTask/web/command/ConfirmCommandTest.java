package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.ConfirmCommand;

class ConfirmCommandTest {

	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private OrderDAO orderDAO = mock(OrderDAO.class);
	private UserDAO userDAO = mock(UserDAO.class);
	private Command confirmCommand = new ConfirmCommand(orderDAO, userDAO);

	@Test
	void shouldThrowExceptionRequestErrorTotalIsNull() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("total")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			confirmCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
		
	}
	
	@Test
	void shouldThrowExceptionRequestErrorTotalIsNotDigit() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("total")).thenReturn("string");
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			confirmCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldReturnPageLoginUserIsNull() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("total")).thenReturn(Integer.toBinaryString(anyInt()));
		when(session.getAttribute("user")).thenReturn(null);
		
		confirmCommand.execute(request, response);
		
		verify(request, never()).getParameter("city");
	}
	
	@Test
	void shouldReturnRequestToEnterCityNull() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("total")).thenReturn("1");
		when(session.getAttribute("user")).thenReturn(new User());
		when(request.getParameter("city")).thenReturn(null);
		
		confirmCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyCity", true);
	}
	
	@Test
	void shouldReturnRequestToEnterCityEmpty() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("total")).thenReturn("1");
		when(session.getAttribute("user")).thenReturn(new User());
		when(request.getParameter("city")).thenReturn("");
		
		confirmCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyCity", true);
	}
	
}
