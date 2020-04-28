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

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.SortCommand;

class SortCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command sortCommand = new SortCommand(productDAO);

	@Test
	void shouldReturnPageChoiceGenderIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(null);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);

		sortCommand.execute(request, response);

		verify(request, never()).getParameter(any());
	}

	@Test
	void shouldReturnPageChoiceTypeIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(null);

		sortCommand.execute(request, response);

		verify(request, never()).getParameter(any());
	}

	@Test
	void shouldAssignDefaultNumberAndSorterIsNull() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);
		when(request.getParameter("from")).thenReturn("");
		when(request.getParameter("to")).thenReturn("");

		sortCommand.execute(request, response);

		verify(productDAO).findAllProduct(1, Integer.MAX_VALUE, Gender.FEMALE, Type.WINTER);
		verify(session).setAttribute(eq("products"), any());
	}
	
	@Test
	void shouldAssignDefaultNumberAndSorterIsEmpty() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);
		when(request.getParameter("from")).thenReturn("");
		when(request.getParameter("to")).thenReturn("");
		when(request.getParameter("select")).thenReturn("");

		sortCommand.execute(request, response);

		verify(productDAO).findAllProduct(1, Integer.MAX_VALUE, Gender.FEMALE, Type.WINTER);
		verify(session).setAttribute(eq("products"), any());
	}
	
	@Test
	void shouldThrowExceptionCannotFindSorter() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);
		when(request.getParameter("from")).thenReturn("2");
		when(request.getParameter("to")).thenReturn("3");
		when(request.getParameter("select")).thenReturn("string");

		Throwable thrown = assertThrows(AppException.class, () -> {
			sortCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_FIND_SORTER);
	}
	
	@Test
	void shouldAssignSorter() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);
		when(request.getParameter("from")).thenReturn("2");
		when(request.getParameter("to")).thenReturn("3");
		when(request.getParameter("select")).thenReturn("CompareById");

		sortCommand.execute(request, response);

		verify(session).setAttribute(eq("products"), any());
	}

}
