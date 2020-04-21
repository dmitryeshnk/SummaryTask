package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareBySize extends CompareBy {

	@Override
	public int compare(Product o1, Product o2) {
		return o1.getSize() - o2.getSize();
	}

}
