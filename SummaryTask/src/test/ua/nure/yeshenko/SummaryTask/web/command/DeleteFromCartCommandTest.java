package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.DeleteFromCartCommand;

class DeleteFromCartCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command deleteFromCartCommand = new DeleteFromCartCommand(productDAO);
	
	@Test
	void shouldThrowExceptionRequestErrorCartIsEmpty() {
		when(request.getSession()).thenReturn(session);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteFromCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorIdIsNull() {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteFromCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionCannotObtainProductById() {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(null);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteFromCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_OBTAIN_PRODUCT_BY_ID);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorNoProductInCart() {
		CartBean cart = CartBean.get(session);
		Product prod1 = new Product();
		cart.addItem(prod1);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		Product prod2 = new Product();
		prod2.setId((long) 1);
		when(productDAO.findProduct(anyLong())).thenReturn(prod2);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			deleteFromCartCommand.execute(request, response);
		});
		
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldDeleteProductFromCart() throws Exception {
		CartBean cart = CartBean.get(session);
		Product product = new Product();
		cart.addItem(product);
		
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		
		deleteFromCartCommand.execute(request, response);
		
		verify(productDAO).updateProduct(product, 1);
		verify(session, times(3)).setAttribute(anyString(), any());
	}

}
