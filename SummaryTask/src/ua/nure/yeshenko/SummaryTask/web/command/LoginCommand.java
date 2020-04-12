package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;
import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Role;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;
import ua.nure.yeshenko.SummaryTask.util.EmailValidator;

public class LoginCommand extends Command {

	private static final long serialVersionUID = -3702935524224767740L;
	private static final Logger log = Logger.getLogger(LoginCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command starts");
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		log.trace("Request parameter: email --> " + email);

		String password = request.getParameter("password");

		String forward = Path.PAGE_LOGIN;
		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			session.setAttribute("isEmptyLogin", true);
			log.trace("Empty fields in users request");
			return createForwardResult(forward);
		}

		if (!new EmailValidator().validate(email)) {
			session.setAttribute("isInvalidEmail", true);
			log.trace("Email invalid");
			return createForwardResult(forward);
		}

		User user = UserDAO.getInstance().findUser(email);
		log.trace("Found in DB: user --> " + user);
		
		if (user == null || !password.equals(user.getPassword())) {
			session.setAttribute("isIncorrectUser", true);
			log.trace("Cannot find user with such email/password");
			return createForwardResult(forward);
		}
		
		Role userRole = Role.getRole(user);
		log.trace("userRole --> " + userRole);
		
		session.setAttribute("user", user);
		log.trace("Set the session attribute: user --> " + user);

		
		session.setAttribute("userRole", userRole);
		log.trace("Set the session attribute: userRole --> " + userRole);
		
		log.info("User " + user + " logged as " + userRole.getName());

		log.debug("Command finished");
		return createRedirectResult(Path.COMMAND__CATALOG);
	}
}
