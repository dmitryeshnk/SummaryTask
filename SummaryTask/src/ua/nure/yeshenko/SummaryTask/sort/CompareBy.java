package ua.nure.yeshenko.SummaryTask.sort;

import java.io.Serializable;
import java.util.Comparator;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CompareBy implements Comparator<Product>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1822735717340059541L;
	private String value = "";
	
	
	public CompareBy(String value) {
		if(value != null) {
			this.value = value;
		}
	}


	public int compare(Product bean1, Product bean2) {
		switch(value) {
		case "0":
			return bean1.getName().toLowerCase().compareTo(bean2.getName().toLowerCase());
		case "1":
			return bean2.getName().toLowerCase().compareTo(bean1.getName().toLowerCase());
		case "2":
			return bean1.getPrice() - bean2.getPrice();
		case "3":
			return bean2.getPrice() - bean1.getPrice();
		case "4":
			return (int) (bean2.getId() - bean1.getId());
		case "5":
			return bean2.getSize() - bean1.getSize();
		case "6":
			return bean1.getSize() - bean2.getSize();
			default:
				return (int) (bean1.getId() - bean2.getId());
		}
		
	}
}
