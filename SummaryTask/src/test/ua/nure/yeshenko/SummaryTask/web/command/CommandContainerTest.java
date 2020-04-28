package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Test;


import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.web.command.CatalogCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.CommandContainer;
import ua.nure.yeshenko.SummaryTask.web.command.LogoutCommand;
import ua.nure.yeshenko.SummaryTask.web.command.NoCommand;

class CommandContainerTest {
	private ServletContext context = mock(ServletContext.class);
	private CommandContainer commandContainer;
	
	@Test
	void initTest() {
		when(context.getAttribute("ProductDAO")).thenReturn(mock(ProductDAO.class));
		when(context.getAttribute("UserDAO")).thenReturn(mock(UserDAO.class));
		when(context.getAttribute("OrderDAO")).thenReturn(mock(OrderDAO.class));
		
		new CommandContainer(context);
	}
	
	@Test
	void getTestParameterCommandIsNull() {
		when(context.getAttribute("ProductDAO")).thenReturn(mock(ProductDAO.class));
		when(context.getAttribute("UserDAO")).thenReturn(mock(UserDAO.class));
		when(context.getAttribute("OrderDAO")).thenReturn(mock(OrderDAO.class));
		
		commandContainer = new CommandContainer(context);
		
		Command command = commandContainer.get(null);
		
		assertTrue(command.getClass().isInstance(new NoCommand()));
	}
	
	@Test
	void getTestParameterCommandDidNotFit() {
		when(context.getAttribute("ProductDAO")).thenReturn(mock(ProductDAO.class));
		when(context.getAttribute("UserDAO")).thenReturn(mock(UserDAO.class));
		when(context.getAttribute("OrderDAO")).thenReturn(mock(OrderDAO.class));
		
		commandContainer = new CommandContainer(context);
		
		Command command = commandContainer.get("string");
		
		assertTrue(command.getClass().isInstance(new NoCommand()));
	}
	
	@Test
	void getTestParameterCommand() {
		when(context.getAttribute("ProductDAO")).thenReturn(mock(ProductDAO.class));
		when(context.getAttribute("UserDAO")).thenReturn(mock(UserDAO.class));
		when(context.getAttribute("OrderDAO")).thenReturn(mock(OrderDAO.class));
		
		commandContainer = new CommandContainer(context);
		
		Command command = commandContainer.get("logout");
		
		assertTrue(command.getClass().isInstance(new LogoutCommand()));
		
	}

}
