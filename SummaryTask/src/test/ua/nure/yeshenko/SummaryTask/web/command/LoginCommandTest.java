package test.ua.nure.yeshenko.SummaryTask.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.LoginCommand;

class LoginCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private UserDAO userDAO = mock(UserDAO.class);
	private Command loginCommand = new LoginCommand(userDAO);
	
	@Test
	void shouldDoSendingEmptyEmail() throws Exception {
		String emptyField = "";
		String notEmptyField = "notEmptyField";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(emptyField);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyLogin", true);
	}
	@Test
	void shouldDoSendingEmptyPassword() throws Exception {
		String emptyField = "";
		String notEmptyField = "notEmptyField";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(emptyField);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyLogin", true);
	}
	@Test
	void shouldDoSendingNullEmail() throws Exception {
		String notEmptyField = "notEmptyField";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(null);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyLogin", true);
	}
	@Test
	void shouldDoSendingNullPassword() throws Exception {
		String notEmptyField = "notEmptyField";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(null);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isEmptyLogin", true);
	}
	@Test
	void shouldDoSendingInvalidEmail() throws Exception {
		String invalidEmail = "Admin@";
		String notEmptyField = "notEmptyField";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(invalidEmail);
		when(request.getParameter("name")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isInvalidEmail", true);
	}
	@Test
	void shouldUploadNullUserFromDataBase() throws Exception {
		String validEmail = "admin@a.aa";
		String notEmptyField = "notEmptyField";
		
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(validEmail);
		when(request.getParameter("name")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		when(userDAO.findUser(validEmail)).thenReturn(null);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isIncorrectUser", true);
	}
	@Test
	void shouldUploadUserFromDataBaseWrongPassword() throws Exception {
		String validEmail = "admin@a.aa";
		String notEmptyField = "notEmptyField";
		
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(validEmail);
		when(request.getParameter("name")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		User user = new User();
		user.setPassword(anyString());
		when(userDAO.findUser(validEmail)).thenReturn(user);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("isIncorrectUser", true);
	}
	@Test
	void shouldUserLogin() throws Exception {
		String validEmail = "admin@a.aa";
		String notEmptyField = "notEmptyField";
		
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("email")).thenReturn(validEmail);
		when(request.getParameter("name")).thenReturn(notEmptyField);
		when(request.getParameter("password")).thenReturn(notEmptyField);
		User user = new User();
		user.setPassword(notEmptyField);
		Role userRole = Role.CLIENT;
		user.setRoleId(userRole.ordinal());
		when(userDAO.findUser(validEmail)).thenReturn(user);
		
		loginCommand.execute(request, response);
		
		verify(session).setAttribute("user", user);
		verify(session).setAttribute("userRole", userRole);
	}
		
}
