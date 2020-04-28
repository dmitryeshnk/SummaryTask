package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.Role;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class ChangeRoleUserCommand extends Command {
	private static final Logger log = Logger.getLogger(ChangeRoleUserCommand.class);
	
	private UserDAO userDAO;

	public ChangeRoleUserCommand(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		log.debug("Command start");
		long userId;
		try {
			userId = Long.valueOf(request.getParameter("id"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR, e);
		}
		log.trace("Get request parameter: id --> " + userId);
		
		User user = userDAO.findUser(userId);
		log.trace("Find user in DB --> " + user);
		
		String block = request.getParameter("block");
		if(block == null || block.isEmpty()) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		
		if("true".equals(block)) {
			user.setRoleId(Role.BLOCKED.ordinal());
		} else if("false".equals(block)) {
			user.setRoleId(Role.CLIENT.ordinal());
		} else {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		userDAO.updateUser(user);

		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND_LIST_CLIENTS);
	}

}
