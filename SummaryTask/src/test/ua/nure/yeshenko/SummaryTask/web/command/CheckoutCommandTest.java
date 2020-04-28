package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.CheckoutCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class CheckoutCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private Command checkoutCommand = new CheckoutCommand();
	
	@Test
	void shouldReturnPageLogin() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(null);
		
		checkoutCommand.execute(request, response);
		
		verify(session, never()).getAttribute("userRole");
	}
	
	@Test
	void shouldThrowExceptionUserIsBlocked() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new User());
		when(session.getAttribute("userRole")).thenReturn(Role.BLOCKED);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			checkoutCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_USER_IS_BLOCKED);
	}
	
	@Test
	void shouldSendEmptyCart() throws Exception {
		CartBean cart = mock(CartBean.class);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new User());
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		doReturn(cart.getCart()).when(cart).getCart();
		when(cart.getCart()).thenReturn(Collections.emptyMap());
		
		checkoutCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyCart", true);
	}
	
	@Test
	void shouldSendCart() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new User());
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(session.getAttribute("cart")).thenReturn(cart);
		
		checkoutCommand.execute(request, response);
		
		verify(session).setAttribute("cart", cart);
		verify(session).setAttribute(eq("city"), anyString());
		
	}

}
