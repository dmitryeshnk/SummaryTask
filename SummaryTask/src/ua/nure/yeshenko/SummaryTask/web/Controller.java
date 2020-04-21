package ua.nure.yeshenko.SummaryTask.web;

import static ua.nure.yeshenko.SummaryTask.util.RequestResponceUtil.createForwardResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.nure.yeshenko.SummaryTask.Path;
import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.Operation;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;
import ua.nure.yeshenko.SummaryTask.web.command.Command;
import ua.nure.yeshenko.SummaryTask.web.command.CommandContainer;

/**
 * Main Servlet Controller
 *
 * @author D.Yeshenko
 *
 */
@MultipartConfig
public class Controller extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8394525137813257077L;
	
	private static final Logger LOG = Logger.getLogger(Controller.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	/**
	 * Main method of this controller.
	 */
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		LOG.debug("Controller starts");

		// extract command name from the request
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);

		// obtain command object by its name
		CommandContainer commandContainer = (CommandContainer) getServletContext().getAttribute("CommandContainer");
		Command command = commandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		RequestResult result = createForwardResult(Path.PAGE_ERROR);
		try {
			result = command.execute(request, response);
		} catch (AppException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
		}
		LOG.trace("Forward address --> " + result.getPath());

		LOG.debug("Controller finished, now go to forward address --> " + result.getPath());
		
		if (Operation.FORWARD.equals(result.getOperation())) {
			request.getRequestDispatcher(result.getPath()).forward(request, response);
		} else if(Operation.REDIRECT.equals(result.getOperation())){
			response.sendRedirect(result.getPath());
		}
		
	}
}
