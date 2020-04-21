package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareByPrice extends CompareBy {

	@Override
	public int compare(Product o1, Product o2) {
		return o1.getPrice() - o2.getPrice();
	}

}
