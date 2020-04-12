package ua.nure.yeshenko.SummaryTask.db;


public enum Gender {
	MALE, FEMALE;

	public String getName() {
		return name().toLowerCase();
	}
}
