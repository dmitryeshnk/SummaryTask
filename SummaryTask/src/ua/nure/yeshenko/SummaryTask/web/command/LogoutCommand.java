package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;
import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

public class LogoutCommand extends Command {
	@Override
	public RequestResult execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}
		return createForwardResult(Path.PAGE_LOGIN);
	}

}
