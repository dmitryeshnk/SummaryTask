package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Status;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.SetOrderStatusCommand;

class SetOrderStatusCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private OrderDAO orderDAO = mock(OrderDAO.class);
	private Command setOrderStatusCommand = new SetOrderStatusCommand(orderDAO);

	@Test
	void shouldThrowExceptionRequestErrorStatusIsNull() {
		when(request.getParameter("status")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorStatusIsEmpty() {
		when(request.getParameter("status")).thenReturn("");

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorIdIsNull() {
		when(request.getParameter("status")).thenReturn(Status.PAID.getName());
		when(request.getParameter("id")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorIdDidNotFit() {
		when(request.getParameter("status")).thenReturn(Status.PAID.getName());
		when(request.getParameter("id")).thenReturn("string");

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionCannotObtainOrder() {
		when(request.getParameter("status")).thenReturn(Status.PAID.getName());
		when(request.getParameter("id")).thenReturn("1");
		when(orderDAO.findOrder(any())).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_OBTAIN_ORDER);
	}

	@Test
	void shouldThrowExceptionRequestErrorStatusDidNotFit() {
		when(request.getParameter("status")).thenReturn("string");
		when(request.getParameter("id")).thenReturn("1");
		when(orderDAO.findOrder(any())).thenReturn(new Order());

		Throwable thrown = assertThrows(AppException.class, () -> {
			setOrderStatusCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldSetOrderStatus() throws Exception {
		when(request.getParameter("status")).thenReturn(Status.CANCELED.getName());
		when(request.getParameter("id")).thenReturn("1");
		when(orderDAO.findOrder(any())).thenReturn(new Order());

		setOrderStatusCommand.execute(request, response);

		verify(orderDAO).updateOrder(any(Order.class));
	}

}
