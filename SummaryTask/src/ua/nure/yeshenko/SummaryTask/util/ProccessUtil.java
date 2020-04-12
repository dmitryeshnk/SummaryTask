package ua.nure.yeshenko.SummaryTask.util;

import ua.nure.yeshenko.SummaryTask.model.Operation;
import ua.nure.yeshenko.SummaryTask.model.ProcessResult;

public class ProccessUtil {
	private static final String APP_PREFIX = "/SummaryTask";

	public static ProcessResult createForwardResult(String path) {
		return new ProcessResult(Operation.FORWARD, path);
	}
	
	public static ProcessResult createRedirectResult(String path) {
		return new ProcessResult(Operation.REDIRECT, APP_PREFIX + path);
	}
}
