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

public class RegisterCommand extends Command {
	private static final Logger log = Logger.getLogger(RegisterCommand.class);

	private UserDAO userDAO;

	public RegisterCommand(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.debug("Command starts");

		HttpSession session = request.getSession();

		String email = request.getParameter("email");
		log.trace("Request parameter: email --> " + email);

		String name = request.getParameter("name");

		String password = request.getParameter("password");
		
		if ((email == null | password == null | name == null) || (email.isEmpty() | name.isEmpty() 
				| password.isEmpty())) {
			session.setAttribute("isEmptyRegistration", true);
			log.trace("Empty fields in users request");
			return createForwardResult(Path.PAGE_REGISTRATION);
		}

		if (!email.matches(EmailValidator.EMAIL_PATTERN)) {
			session.setAttribute("isWrongEmail", true);
			log.trace("Email invalid");
			return createForwardResult(Path.PAGE_REGISTRATION);
		}

		if (userDAO.findUser(email) != null) {
			session.setAttribute("isUserAlreadyExist", true);
			log.trace("User " + email + " already exists. Returning to registration");
			return createForwardResult(Path.PAGE_REGISTRATION);
		}
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		System.out.println(Util.hash(password, "SHA-256"));
		user.setPassword(Util.hash(password, "SHA-256"));
		userDAO.createUser(user);
		log.info("User " + user.getEmail() + " registered.");

		user = userDAO.findUser(user.getEmail());

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
