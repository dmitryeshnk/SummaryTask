package ua.nure.yeshenko.SummaryTask.web.command;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public abstract class Command {
	public abstract RequestResult execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException;
}
