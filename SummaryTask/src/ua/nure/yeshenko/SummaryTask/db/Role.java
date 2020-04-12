package ua.nure.yeshenko.SummaryTask.db;

import ua.nure.yeshenko.SummaryTask.db.entity.User;

public enum Role {
	ADMIN, CLIENT, BLOCKED;
	
	public static  Role getRole(User user) {
		int roleId = user.getRoleId();
		return Role.values()[roleId];
	}
	
	public String getName() {
		return name().toLowerCase();
	}
}
