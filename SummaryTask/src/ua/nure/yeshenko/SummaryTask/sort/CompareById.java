package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareById extends CompareBy {
	
	@Override
	public int compare(Product o1, Product o2) {
		return (int) (o1.getId() - o2.getId());
	}

}
