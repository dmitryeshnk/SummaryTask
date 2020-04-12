package ua.nure.yeshenko.SummaryTask.web.command;

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
import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;
import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

public class RegisterCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2584645765009052911L;
	private static final Logger log = Logger.getLogger(RegisterCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command starts");

		HttpSession session = request.getSession();

		String email = request.getParameter("email");
		log.trace("Request parameter: email --> " + email);

		String name = request.getParameter("name");

		String password = request.getParameter("password");

		String forward = Path.PAGE_REGISTRATION;
		if (email == null || password == null || name == null || email.isEmpty() || name.isEmpty()
				|| password.isEmpty()) {
			session.setAttribute("isEmptyRegistration", true);
			log.trace("Empty fields in users request");
			return createForwardResult(forward);
		}

		if (!new EmailValidator().validate(email)) {
			session.setAttribute("isWrongEmail", true);
			log.trace("Email invalid");
			return createForwardResult(forward);
		}
		
		UserDAO manager = UserDAO.getInstance();

		if (manager.findUser(email) != null) {
			session.setAttribute("isUserAlreadyExist", true);
			log.trace("User " + email + " already exists. Returning to registration");
			return createForwardResult(forward);
		}
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		manager.createUser(user);
		log.info("User " + user.getEmail() + " registered.");
		
		user = manager.findUser(user.getEmail());
		
		Role userRole = Role.getRole(user);
		log.trace("userRole --> " + userRole);
		if(userRole == Role.CLIENT) {
			forward = Path.COMMAND__CATALOG;
		} 
		
		if(userRole == Role.ADMIN) {
			forward = Path.COMMAND__LIST_ORDERS;
		}

		session.setAttribute("user", user);
		log.trace("Set the session attribute: user --> " + user);
		
		session.setAttribute("userRole", userRole);
		log.trace("Set the session attribute: userRole --> " + userRole);
		
		log.info("User " + user + " logged as " + userRole.getName());
		log.debug("Command finished");
		return createRedirectResult(forward);
	}

}
