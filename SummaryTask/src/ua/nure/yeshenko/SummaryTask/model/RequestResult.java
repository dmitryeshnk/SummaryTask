package ua.nure.yeshenko.SummaryTask.model;

public class RequestResult {
	private Operation operation;
	
	private String path;
	
	public RequestResult(Operation operation, String path) {
		this.operation = operation;
		this.path = path;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
