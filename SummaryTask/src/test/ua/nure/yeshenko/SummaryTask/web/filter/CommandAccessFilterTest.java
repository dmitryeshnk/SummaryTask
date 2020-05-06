package test.ua.nure.yeshenko.SummaryTask.web.filter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.web.filter.CommandAccessFilter;

class CommandAccessFilterTest {
	private FilterConfig fConfig = mock(FilterConfig.class);
	private HttpServletRequest request = mock(HttpServletRequest.class);
	private HttpServletResponse response = mock(HttpServletResponse.class);
	private FilterChain chain = mock(FilterChain.class);
	private RequestDispatcher dispatcher = mock(RequestDispatcher.class);
	private CommandAccessFilter filter = new CommandAccessFilter();
	
	@Test
	void initTest() throws ServletException {
		when(fConfig.getInitParameter(anyString())).thenReturn("str qwe zxc");
		
		filter.init(fConfig);
	}
	
	@Test
	void doFilterCommandIsNullTest() throws ServletException, IOException {
		when(request.getParameter("command")).thenReturn(null);
		when(request.getRequestDispatcher(Path.PAGE_LOGIN)).thenReturn(dispatcher);
		
		filter.doFilter(request, response, chain);
		
		verify(request).setAttribute(any(), any());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void doFilterCommandIsEmptyTest() throws ServletException, IOException {
		when(request.getParameter("command")).thenReturn("");
		when(request.getRequestDispatcher(Path.PAGE_LOGIN)).thenReturn(dispatcher);
		
		filter.doFilter(request, response, chain);
		
		verify(request).setAttribute(any(), any());
		verify(dispatcher).forward(request, response);
	}
	
	@Test
	void doFilterCommandIsOutOfControlTest() throws ServletException, IOException {
		when(fConfig.getInitParameter(anyString())).thenReturn("str qwe zxc");
		when(request.getParameter("command")).thenReturn("str");

		filter.init(fConfig);
		filter.doFilter(request, response, chain);
		filter.destroy();
		
		verify(chain).doFilter(request, response);
	}
	
	@Test
	void doFilterSessionIsNullTest() throws ServletException, IOException {
		when(request.getParameter("command")).thenReturn("asd");
		when(request.getSession()).thenReturn(null);
		when(request.getRequestDispatcher(Path.PAGE_LOGIN)).thenReturn(dispatcher);
		
		filter.doFilter(request, response, chain);
		
		verify(request).setAttribute(any(), any());
		verify(dispatcher).forward(request, response);
	}
	
}
