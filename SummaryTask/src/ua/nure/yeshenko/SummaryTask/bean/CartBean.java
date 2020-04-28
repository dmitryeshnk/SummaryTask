package ua.nure.yeshenko.SummaryTask.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import ua.nure.yeshenko.SummaryTask.db.entity.Product;

public class CartBean {
	Map<Product, Integer> cart = new ConcurrentHashMap<>();

	public void addItem(Product product) {
		if (cart != null) {
			if(cart.get(product) == null) {
				cart.put(product, 1);
			} else {
				cart.put(product, cart.get(product) + 1);
			}
		} 
	}

	public void deleteItem(Product product) {
		if (cart != null) {
			if(cart.get(product) != null) {
				cart.put(product, cart.get(product) - 1);
			}
		}
	}
	
	public Integer deleteItem(Product product, boolean bool) {
		if (cart != null) {
			return cart.remove(product);
		}
		return null;
	}

	public Map<Product, Integer> getCart() {
		if (cart.isEmpty() || cart == null) {
			return new ConcurrentHashMap<>();
		}
		return cart;
	}

	public static CartBean get(HttpSession session) {
		CartBean cart = (CartBean) session.getAttribute("cart");
		if (cart == null) {
			cart = new CartBean();
			session.setAttribute("cart", cart);
		}
		return cart;
	}

	public int getSize() {
		if (cart.isEmpty()) {
			return 0;
		}
		return cart.size();
	}

	private List<Long> getAllId() {
		List<Long> allId = new ArrayList<>();
		for (Product pr : cart.keySet()) {
			allId.add(pr.getId());
		}
		return allId;
	}

	public void clear() {
		if (cart != null || !(cart.isEmpty())) {
			cart.clear();
		}
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (Long l : getAllId()) {
			buff.append(l + "&");
		}
		if(buff.length() > 0) {
			return buff.toString().substring(0, buff.length() - 1);
		} 
		return buff.toString();
	}

}
