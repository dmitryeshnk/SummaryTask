package ua.nure.yeshenko.SummaryTask.web.command;

import static ua.nure.yeshenko.SummaryTask.util.ProccessUtil.createForwardResult;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class CartCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4208580498315557721L;
	private static final Logger log = Logger.getLogger(CartCommand.class);
	
	@Override
	public ProcessResult execute() throws IOException, ServletException {
		log.debug("Command start");
		String forward = Path.PAGE_CART;
		log.debug("Command finish");
		return createForwardResult(forward);
	}

}
