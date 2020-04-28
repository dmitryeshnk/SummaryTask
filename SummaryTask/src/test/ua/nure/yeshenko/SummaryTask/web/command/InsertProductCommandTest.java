package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.InsertProductCommand;

class InsertProductCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command insertProductCommand = new InsertProductCommand(productDAO);

	@Test
	void shouldThrowExceptionParameterDidNotFit() {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter(any())).thenReturn(null);

		Throwable thrown = assertThrows(AppException.class, () -> {
			insertProductCommand.execute(request, response);
		});

		assertEquals(thrown.getMessage(), Messages.ERR_CANNOT_INSERT_PRODUCT);
	}

	@Test
	void shouldInsertNewProduct() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(request.getParameter(anyString())).thenReturn("1");
		when(session.getAttribute("gender")).thenReturn(Gender.FEMALE);
		when(session.getAttribute("type")).thenReturn(Type.WINTER);
		when(request.getPart("image")).thenReturn(mock(Part.class));
		
		insertProductCommand.execute(request, response);
		
		verify(productDAO).insertProduct(any());
	}

}
