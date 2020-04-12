package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;
import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.db.Type;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Gender;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class ChoiceCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3208397232621889841L;
	private static final Logger log = Logger.getLogger(ChoiceCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		HttpSession session = request.getSession();
		if (request.getParameter("break") != null && request.getParameter("break").equals("gender")) {
			session.setAttribute("gender", null);
			session.setAttribute("type", null);
			return createRedirectResult(Path.PAGE__CHOICE);
		} else if (request.getParameter("break") != null && request.getParameter("break").equals("type")) {
			session.setAttribute("type", null);
			return createForwardResult(Path.PAGE__CHOICE);
		}

		String gender = request.getParameter("gender");

		log.trace("Request parameter: gender --> " + gender);
		if (!(gender == null || gender.isEmpty())) {
			try {
				Gender gender_ = Gender.valueOf(gender.toUpperCase());
				session.setAttribute("gender", gender_);
				log.trace("Set the session attribute: gender --> " + gender);
			} catch(Exception ex) {
				log.error(Messages.ERR_REQUEST_ERROR + ex);
				throw new AppException(Messages.ERR_REQUEST_ERROR + ex);
			}
			return createForwardResult(Path.PAGE__CHOICE);
		} else if (session.getAttribute("gender") == null) {
			return createForwardResult(Path.PAGE__CHOICE);
		}
		String type = request.getParameter("type");
		log.trace("Request parameter: type --> " + type);

		if (type == null || type.isEmpty()) {
			return createRedirectResult(Path.PAGE__CHOICE);
		}
		try {
			Type type_ = Type.valueOf(type.toUpperCase());
			session.setAttribute("type", type_);
			log.trace("Set the session attribute: type --> " + type_);
		} catch(Exception ex) {
			log.error(Messages.ERR_REQUEST_ERROR + ex);
			throw new AppException(Messages.ERR_REQUEST_ERROR + ex);
		}
		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__CATALOG);
	}

}
