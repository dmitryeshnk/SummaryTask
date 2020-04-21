package ua.nure.yeshenko.SummaryTask.sort;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareBySizeReverse extends CompareBy {

	@Override
	public int compare(Product o1, Product o2) {
		return o2.getSize() - o1.getSize();
	}

}
