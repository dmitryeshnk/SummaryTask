package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import ua.nure.yeshenko.SummaryTask.web.command.ChangeQuantityCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class ChangeQuantityCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command changeQuantityCommand = new ChangeQuantityCommand(productDAO);

	@Test
	void shouldThrowExceptionRequestErrorCartIsEmpty() throws Exception {
		CartBean cart = mock(CartBean.class);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeQuantityCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorIdIsNull() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeQuantityCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorIdIsString() throws Exception {
		CartBean cart = CartBean.get(session);
		cart.addItem(new Product());

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("string");

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeQuantityCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldThrowExceptionRequestErrorChangeDidNotFit() throws Exception {
		CartBean cart = CartBean.get(session);
		Product product = new Product();
		cart.addItem(product);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		when(request.getParameter("change")).thenReturn("string");

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeQuantityCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldUpdateProductDeleteFromCart() throws Exception {
		CartBean cart = CartBean.get(session);
		Product product = new Product();
		cart.addItem(product);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		when(request.getParameter("change")).thenReturn("subtract");

		changeQuantityCommand.execute(request, response);

		verify(productDAO).updateProduct(product, 1);
		verify(session, times(3)).setAttribute(any(), any());
	}
	
	@Test
	void shouldUpdateProductAddToCart() throws Exception {
		CartBean cart = CartBean.get(session);
		Product product = new Product();
		cart.addItem(product);

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("cart")).thenReturn(cart);
		when(request.getParameter("id")).thenReturn("1");
		when(productDAO.findProduct(anyLong())).thenReturn(product);
		when(request.getParameter("change")).thenReturn("add");

		changeQuantityCommand.execute(request, response);

		verify(productDAO).updateProduct(product, -1);
		verify(session, times(3)).setAttribute(any(), any());
	}
}
