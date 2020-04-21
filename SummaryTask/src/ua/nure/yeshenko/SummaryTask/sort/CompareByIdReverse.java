package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareByIdReverse extends CompareBy {

	@Override
	public int compare(Product o1, Product o2) {
		return (int) (o2.getId() - o1.getId());
	}

}
