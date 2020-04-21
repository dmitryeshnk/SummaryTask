package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class ChangeLocaleCommand extends Command{
	private static final Logger log = Logger.getLogger(ChangeLocaleCommand.class);

	@Override
	public RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		String newLocale = request.getParameter("newLocale");
		log.trace("Get request parameter: newLocale -->" + newLocale);
		session.setAttribute("locale", newLocale);
		log.trace("Session locale -->" + session.getAttribute("locale"));
		
		log.debug("Command finish");
		return createRedirectResult(request.getHeader("referer").toString().replaceAll("http://localhost:8080/SummaryTask", ""));
	}

}
