package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class ChangeLocaleCommand extends Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8891310583007745194L;
	
	private static final Logger log = Logger.getLogger(ChangeLocaleCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
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
