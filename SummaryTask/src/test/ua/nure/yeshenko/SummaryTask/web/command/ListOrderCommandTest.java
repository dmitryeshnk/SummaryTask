package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Order;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.ListOrderCommand;

class ListOrderCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private OrderDAO orderDAO = mock(OrderDAO.class);
	private Command listOrderCommand = new ListOrderCommand(orderDAO, productDAO);
	
	@Test
	void shouldReturnPageOrdersIsEmptyRoleAdmin() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
		when(orderDAO.findAllOrders()).thenReturn(Collections.emptyList());
		
		listOrderCommand.execute(request, response);
		
		verify(session).setAttribute("isOrderEmpty", true);
	}
	
	@Test
	void shouldReturnPageOrdersIsNullRoleAdmin() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.ADMIN);
		when(orderDAO.findAllOrders()).thenReturn(null);
		
		listOrderCommand.execute(request, response);
		
		verify(session).setAttribute("isOrderEmpty", true);
	}
	
	@Test
	void shouldReturnPageOrdersIsEmptyRoleClient() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(new User());
		when(orderDAO.findAllOrders(any())).thenReturn(Collections.emptyList());
		
		listOrderCommand.execute(request, response);
		
		verify(session).setAttribute("isOrderEmpty", true);
	}
	
	@Test
	void shouldReturnPageOrdersIsNullRoleClient() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(session.getAttribute("user")).thenReturn(new User());
		when(orderDAO.findAllOrders(any())).thenReturn(null);
		
		listOrderCommand.execute(request, response);
		
		verify(session).setAttribute("isOrderEmpty", true);
	}
	
	@Test
	void shouldReturnPageListOrders() throws Exception {
		List<Order> orders = new ArrayList<>();
		Order order = new Order();
		order.setId((long) 1);
		order.setProductsId("1&2&3");
		orders.add(order);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("userRole")).thenReturn(Role.CLIENT);
		when(orderDAO.findAllOrders(any())).thenReturn(orders);
		when(productDAO.findProduct(anyLong())).thenReturn(new Product());
		
		listOrderCommand.execute(request, response);
		
		verify(session, times(3)).setAttribute(any(), any());
		
	}
}
