package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;
import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;
import ua.nure.yeshenko.SummaryTask.util.EmailValidator;
import ua.nure.yeshenko.SummaryTask.util.Util;

public class LoginCommand extends Command {
	private static final Logger log = Logger.getLogger(LoginCommand.class);
	
	private UserDAO userDAO;

	public LoginCommand(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command starts");
		HttpSession session = request.getSession();
		String email = request.getParameter("email");
		log.trace("Request parameter: email --> " + email);

		String password = request.getParameter("password");

		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			session.setAttribute("isEmptyLogin", true);
			log.trace("Empty fields in users request");
			return createForwardResult(Path.PAGE_LOGIN);
		}

		if (!email.matches(EmailValidator.EMAIL_PATTERN)) {
			session.setAttribute("isInvalidEmail", true);
			log.trace("Email invalid");
			return createForwardResult(Path.PAGE_LOGIN);
		}		
		User user = userDAO.findUser(email);
		log.trace("Found in DB: user --> " + user);
		
		if (user == null || !Util.hash(password, "SHA-256").equals(user.getPassword())) {
			session.setAttribute("isIncorrectUser", true);
			log.trace("Cannot find user with such email/password");
			return createForwardResult(Path.PAGE_LOGIN);
		}
		
		Role userRole = Role.getRole(user);
		log.trace("userRole --> " + userRole);
		
		session.setAttribute("user", user);
		log.trace("Set the session attribute: user --> " + user);

		
		session.setAttribute("userRole", userRole);
		log.trace("Set the session attribute: userRole --> " + userRole);
		
		log.info("User " + user + " logged as " + userRole.getName());

		log.debug("Command finished");
		return createRedirectResult(Path.COMMAND_CATALOG);
	}
}
