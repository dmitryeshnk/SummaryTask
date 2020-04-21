package ua.nure.yeshenko.SummaryTask.db.entity;

public enum Status {
	REGISTERED, PAID, CANCELED;
	
	public String getName() {
		return name().toLowerCase();
	}
}
