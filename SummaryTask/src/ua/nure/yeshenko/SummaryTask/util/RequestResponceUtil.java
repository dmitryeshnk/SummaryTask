package ua.nure.yeshenko.SummaryTask.util;

import ua.nure.yeshenko.SummaryTask.model.Operation;
import ua.nure.yeshenko.SummaryTask.model.RequestResult;

public class RequestResponceUtil {
	private static final String APP_PREFIX = "/SummaryTask";

	public static RequestResult createForwardResult(String path) {
		return new RequestResult(Operation.FORWARD, path);
	}
	
	public static RequestResult createRedirectResult(String path) {
		return new RequestResult(Operation.REDIRECT, APP_PREFIX + path);
	}
}
