package ua.nure.yeshenko.SummaryTask.db.bean;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CartBean {
	List<Product> cart = new ArrayList<>();
	
	public synchronized void addItem(Product product) {
		cart.add(product);
	}
	
	public synchronized boolean deleteItem(Product product) {
		return cart.remove(product);
	}
	
	public synchronized List<Product> getCart() {
		return new ArrayList<>(cart);
	}
	
	public static CartBean get(HttpSession session ) {
		CartBean cart = (CartBean) session.getAttribute("cart");
		if(cart == null) {
			cart = new CartBean();
			session.setAttribute("cart", cart);
		}
		return cart;
	}
	
	public synchronized int getSize() {
		if(cart.isEmpty()) {
			return 0;
		}
		return cart.size();
	}
	
	private List<Long> getAllId() {
		List<Long> allId = new ArrayList<>();
		for(Product pr:cart) {
			allId.add(pr.getId());
		}
		return allId;
	}
	
	public void clear() {
		cart.clear();
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for(Long l : getAllId()) {
			buff.append(l+"&");
		}
		
		return buff.toString().substring(0, buff.length()-1);
	}
	
	
}
