package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class GetCartCommand extends Command {
	
	@Override
	public RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		return createForwardResult(Path.PAGE_CART);
	}

}
