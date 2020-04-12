package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.yeshenko.SummaryTask.exception.AppException;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public abstract class Command implements Serializable {
	private static final long serialVersionUID = 8879403039606311780L;

	protected ServletContext context;
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	/**
	 * Execution method for command.
	 * 
	 * @return Address to go once the command is executed.
	 */
	public void init(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
		this.context = context;
		this.request = request;
		this.response = response;
	}

	public abstract ProcessResult execute() throws IOException, ServletException, AppException;

}
