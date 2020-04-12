package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createRedirectResult;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.db.Role;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.db.entity.User;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class ChangeRoleUserCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -672758420550779432L;
	private static final Logger log = Logger.getLogger(ChangeRoleUserCommand.class);

	@Override
	public ProcessResult execute() throws IOException, ServletException, AppException {
		log.debug("Command start");
		long userId;
		try {
			userId = Long.valueOf(request.getParameter("id"));
		} catch (Exception e) {
			log.error(Messages.ERR_REQUEST_ERROR + e);
			throw new AppException(Messages.ERR_REQUEST_ERROR + e);
		}
		log.trace("Get request parameter: id --> " + userId);
		
		UserDAO manager = UserDAO.getInstance();
		
		User user = manager.findUser(userId);
		log.trace("Find user in DB --> " + user);
		
		String block = request.getParameter("block");
		if(block == null || block.isEmpty()) {
			log.error(Messages.ERR_REQUEST_ERROR);
			throw new AppException(Messages.ERR_REQUEST_ERROR);
		}
		
		if("true".equals(block)) {
			user.setRoleId(Role.BLOCKED.ordinal());
		} 
		
		if("false".equals(block)) {
			user.setRoleId(Role.CLIENT.ordinal());
		}
		
		manager.updateUser(user);

		log.debug("Command finish");
		return createRedirectResult(Path.COMMAND__LIST_CLIENTS);
	}

}
