package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.OrderToCartCommand;

class OrderToCartCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command orderToCartCommand = new OrderToCartCommand(productDAO);

	@Test
	void shouldThrowExceptionUserIsBlocked() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.BLOCKED);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			orderToCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_USER_IS_BLOCKED);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorIdIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(request.getParameter("id")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			orderToCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorIdDidNotFit() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(request.getParameter("id")).thenReturn("string");
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			orderToCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionCannotObtainProductById() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			orderToCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
	}
	
	@Test
	void shouldOrderProductToCart() throws Exception {
		CartBean cart = CartBean.get(session);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		Product product = new Product(); 
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		
		orderToCartCommand.execute(request, response);
		
		verify(productDAO).updateProduct(product, -cart.getCart().get(product));
		verify(session, times(4)).setAttribute(any(), any());
	}
}
