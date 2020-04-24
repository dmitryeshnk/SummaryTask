package test.ua.nure.yeshenko.SummaryTask.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.ListClientsCommand;

class ListClientsCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private UserDAO userDAO = mock(UserDAO.class);
	private Command listClientsCommand = new ListClientsCommand(userDAO);

	@Test
	void shouldThrowExceptionNotAdmin() throws Exception {
		User user = new User();
		user.setRoleId(Role.CLIENT.ordinal());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(user);
		Throwable thrown = assertThrows(AppException.class, () -> {
			listClientsCommand.execute(request, response);
		});
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldForm—ustomerLists() throws Exception {
		List<User> users = new ArrayList<>();
		User user = new User();
		user.setRoleId(1);
		users.add(user);
		user = new User();
		user.setRoleId(2);
		users.add(user);
		user = new User();
		user.setRoleId(Role.ADMIN.ordinal());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("user")).thenReturn(user);
		when(userDAO.findAllUsers()).thenReturn(users);
		
		listClientsCommand.execute(request, response);
		
		verify(session).setAttribute("whiteList", Collections.singletonList(users.get(0)));
		verify(session).setAttribute("blackList", Collections.singletonList(users.get(1)));
	}

}
