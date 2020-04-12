package ua.nure.yeshenko.SummaryTask.model;

public class ProcessResult {
	private Operation operation;
	
	private String path;
	
	public ProcessResult(Operation operation, String path) {
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
