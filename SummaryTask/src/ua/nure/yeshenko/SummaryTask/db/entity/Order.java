package ua.nure.yeshenko.SummaryTask.db.entity;

public class Order extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -289320117622915135L;
	
	private int cost;
	
	private Long user_id;
	
	private Status status;
	
	private String productsId;

	public String getProductsId() {
		return productsId;
	}

	public void setProductsId(String productsId) {
		this.productsId = productsId;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
