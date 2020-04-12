package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;
import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

public class LogoutCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5272954004495642007L;

	@Override
	public ProcessResult execute() throws IOException, ServletException {
		HttpSession session = request.getSession();
		
		if(session != null) {
			session.invalidate();
		}
		return createForwardResult(Path.PAGE_LOGIN);
	}
	
}
