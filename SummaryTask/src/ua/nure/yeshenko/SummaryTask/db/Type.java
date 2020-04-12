package ua.nure.yeshenko.SummaryTask.db;

public enum Type {
	WINTER, DEMI_SEASON, SUMMER;
	
	public String getName() {
		return name().toLowerCase();
	}
}
