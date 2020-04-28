package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.web.command.ChangeLocaleCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class ChangeLocaleCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private Command changeLocaleCommand = new ChangeLocaleCommand();

	@Test
	void shouldSendNullNewLocale() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("newLocale")).thenReturn(null);
		when(request.getHeader("referer")).thenReturn("");

		changeLocaleCommand.execute(request, response);

		verify(session, never()).setAttribute(any(), any());
	}

	@Test
	void shouldSendEmptyNewLocale() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("newLocale")).thenReturn("");
		when(request.getHeader("referer")).thenReturn("");

		changeLocaleCommand.execute(request, response);

		verify(session, never()).setAttribute(any(), any());
	}

	@Test
	void shouldSendNewLocale() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("newLocale")).thenReturn("newLocale");
		when(request.getHeader("referer")).thenReturn("");

		changeLocaleCommand.execute(request, response);

		verify(session).setAttribute(any(), any());
	}

}
