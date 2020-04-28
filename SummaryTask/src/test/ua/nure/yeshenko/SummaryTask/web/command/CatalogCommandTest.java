package test.ua.nure.yeshenko.SummaryTask.web.command;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Gender;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.db.entity.Type;
import ua.nure.yeshenko.SummaryTask.web.command.CatalogCommand;
import ua.nure.yeshenko.SummaryTask.web.command.Command;

class CatalogCommandTest {
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private HttpSession session = mock(HttpSession.class);
	private ProductDAO productDAO = mock(ProductDAO.class);
	private Command catalogCommand = new CatalogCommand(productDAO);

	@Test
	void shouldDoSendingNullGender() throws Exception {
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(null);

		catalogCommand.execute(request, response);

		verify(session, never()).getAttribute("type");
	}

	@Test
	void shouldDoSendingNullType() throws Exception {
		Gender gender = Gender.FEMALE;
		Type type = null;

		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(gender);
		when(session.getAttribute("type")).thenReturn(type);

		catalogCommand.execute(request, response);

		verify(productDAO, never()).findAllProduct(1, Integer.MAX_VALUE, gender, type);
	}

	@Test
	void shouldGetNullListProduct() throws Exception {
		Gender gender = Gender.FEMALE;
		Type type = Type.DEMI_SEASON;
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(gender);
		when(session.getAttribute("type")).thenReturn(type);
		when(productDAO.findAllProduct(1, Integer.MAX_VALUE, gender, type)).thenReturn(null);

		catalogCommand.execute(request, response);

		verify(session).setAttribute("isNullProduct", true);
	}

	@Test
	void shouldGetEmptyListProduct() throws Exception {
		Gender gender = Gender.FEMALE;
		Type type = Type.DEMI_SEASON;
		List<Product> list = new ArrayList<>();
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(gender);
		when(session.getAttribute("type")).thenReturn(type);
		when(productDAO.findAllProduct(1, Integer.MAX_VALUE, gender, type)).thenReturn(list);

		catalogCommand.execute(request, response);

		verify(session).setAttribute("isNullProduct", true);
	}

	@Test
	void shouldGetListProduct() throws Exception {
		Gender gender = Gender.FEMALE;
		Type type = Type.DEMI_SEASON;
		List<Product> list = new ArrayList<>();
		list.add(new Product());
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("gender")).thenReturn(gender);
		when(session.getAttribute("type")).thenReturn(type);
		when(productDAO.findAllProduct(1, Integer.MAX_VALUE, gender, type)).thenReturn(list);

		catalogCommand.execute(request, response);

		verify(session).setAttribute("products", list);
	}

}
