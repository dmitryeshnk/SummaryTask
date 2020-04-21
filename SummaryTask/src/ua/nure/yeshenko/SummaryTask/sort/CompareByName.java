package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareByName extends CompareBy {
	
	@Override
	public int compare(Product o1, Product o2) {
		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	}
}
