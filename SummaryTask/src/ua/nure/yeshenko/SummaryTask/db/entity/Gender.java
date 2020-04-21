package ua.nure.yeshenko.SummaryTask.db.entity;


public enum Gender {
	MALE, FEMALE;

	public String getName() {
		return name().toLowerCase();
	}
}
