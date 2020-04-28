package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.ChoiceCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class ChoiceCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private Command choiceCommand = new ChoiceCommand();
	
	@Test
	void shouldMakeNullGenderAndType() throws Exception {
		String _break = "gender";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(_break);
		
		choiceCommand.execute(request, response);
		
		verify(session).setAttribute("gender", null);
		verify(session).setAttribute("type", null);
	}
	
	@Test
	void shouldMakeNullType() throws Exception {
		String _break = "type";
		
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(_break);
		
		choiceCommand.execute(request, response);
		
		verify(session).setAttribute("type", null);
	}
	
	@Test
	void shouldSetGenderToChoice() throws Exception {
		String gender = "female";
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(gender);
		
		choiceCommand.execute(request, response);
		
		verify(session).setAttribute("gender", Gender.valueOf(gender.toUpperCase()));
	}
	
	@Test
	void shouldThrowExceptionIncorrectRequestForGender() throws Exception {
		String gender = "anyString";
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(gender);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			choiceCommand.execute(request, response);
		});
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
	@Test
	void shouldReturnToPageChoiceGenderIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(null);
		when(session.getAttribute("gender")).thenReturn(null);
		
		choiceCommand.execute(request, response);
		
		verify(request, never()).getParameter("type");
	}
	
	@Test
	void shouldReturnToPageChoiceTypeIsNull() throws Exception {
		String gender = "female";
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(null);
		when(session.getAttribute("gender")).thenReturn(Gender.valueOf(gender.toUpperCase()));
		when(request.getParameter("type")).thenReturn(null);
		
		choiceCommand.execute(request, response);
		
		verify(session, never()).setAttribute(eq("type"), any());
	}
	
	@Test
	void shouldSetTypeToChoice() throws Exception {
		String gender = "female";
		String type = "winter";
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(null);
		when(session.getAttribute("gender")).thenReturn(Gender.valueOf(gender.toUpperCase()));
		when(request.getParameter("type")).thenReturn(type);
		
		choiceCommand.execute(request, response);
		
		verify(session).setAttribute("type", Type.valueOf(type.toUpperCase()));
	}
	
	@Test
	void shouldThrowExceptionIncorrectRequestForType() throws Exception {
		String gender = "female";
		String type = "anyString";
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("break")).thenReturn(null);
		when(request.getParameter("gender")).thenReturn(null);
		when(session.getAttribute("gender")).thenReturn(Gender.valueOf(gender.toUpperCase()));
		when(request.getParameter("type")).thenReturn(type);
		
		Throwable thrown = assertThrows(AppException.class, () -> {
			choiceCommand.execute(request, response);
		});
		assertEquals(thrown.getMessage(), Messages.ERR_REQUEST_ERROR);
	}
	
}
