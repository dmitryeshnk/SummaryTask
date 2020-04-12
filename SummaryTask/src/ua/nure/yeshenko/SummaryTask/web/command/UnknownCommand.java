package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;

import javax.servlet.ServletException;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

public class UnknownCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5557486946188733346L;

	@Override
	public ProcessResult execute() throws IOException, ServletException {
		return createForwardResult(Path.PAGE_ERROR);
	}

}
