package ua.nure.yeshenko.SummaryTask.sort;

import java.util.Comparator;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public abstract class CompareBy implements Comparator<Product> {
	public abstract int compare(Product o1, Product o2);
}
