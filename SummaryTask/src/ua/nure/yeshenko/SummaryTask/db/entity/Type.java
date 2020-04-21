package ua.nure.yeshenko.SummaryTask.db.entity;

public enum Type {
	WINTER, DEMI_SEASON, SUMMER;
	
	public String getName() {
		return name().toLowerCase();
	}
}
