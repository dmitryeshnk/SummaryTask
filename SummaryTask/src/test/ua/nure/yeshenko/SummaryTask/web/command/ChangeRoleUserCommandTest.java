package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.ChangeRoleUserCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class ChangeRoleUserCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private UserDAO userDAO = mock(UserDAO.class);
	private Command changeRoleUserCommand = new ChangeRoleUserCommand(userDAO);

	@Test
	void shouldThrowExceptionRequestErrorUserIdIsNull() throws Exception {
		when(request.getParameter("id")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeRoleUserCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorParameterBlockIsNull() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(request.getParameter("block")).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeRoleUserCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorParameterBlockIsEmpty() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(userDAO.findUser(anyLong())).thenReturn(new User());
		when(request.getParameter("block")).thenReturn("");

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeRoleUserCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldThrowExceptionRequestErrorParameterBlockDidNotFit() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(userDAO.findUser(anyLong())).thenReturn(new User());
		when(request.getParameter("block")).thenReturn("string");

		Throwable thrown = assertThrows(AppException.class, () -> {
			changeRoleUserCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}

	@Test
	void shouldChangeRoleUserToBlock() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(userDAO.findUser(anyLong())).thenReturn(new User());
		when(request.getParameter("block")).thenReturn("true");

		changeRoleUserCommand.execute(request, response);
		
		userDAO.updateUser(any());
	}
	
	@Test
	void shouldChangeRoleUserToUnblock() throws Exception {
		when(request.getParameter("id")).thenReturn("1");
		when(userDAO.findUser(anyLong())).thenReturn(new User());
		when(request.getParameter("block")).thenReturn("false");

		changeRoleUserCommand.execute(request, response);
		
		userDAO.updateUser(any());
	}
}
