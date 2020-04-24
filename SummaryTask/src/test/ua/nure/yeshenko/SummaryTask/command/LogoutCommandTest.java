package test.ua.nure.yeshenko.SummaryTask.command;

import static org.mockito.Mockito.mock;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.LogoutCommand;

class LogoutCommandTest {
	
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	
	private Command logoutCommand = new LogoutCommand();
	
	@Test
	void shouldInvalidateSessionWhenSessionExists() throws Exception {
		Mockito.when(request.getSession(false)).thenReturn(session);
		
		logoutCommand.execute(request, response);
		
		Mockito.verify(session).invalidate();
	}
	
	@Test
	void shouldNotInvalidateSessionWhenSessionDoesNotExist() throws Exception {
		Mockito.when(request.getSession(false)).thenReturn(null);
		
		logoutCommand.execute(request, response);
		
		Mockito.verify(session, Mockito.never()).invalidate();
	}
	
}
