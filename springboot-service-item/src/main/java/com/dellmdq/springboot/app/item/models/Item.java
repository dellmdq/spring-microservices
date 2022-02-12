package com.dellmdq.springboot.app.item.models;

public class Item {

	private Product prod;
	private Integer quantity;

	public Item() {
	}

	public Item(Product prod, Integer quantity) {
		super();
		this.prod = prod;
		this.quantity = quantity;
	}

	public Product getProd() {
		return prod;
	}

	public void setProd(Product prod) {
		this.prod = prod;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Double getTotal() {
		return prod.getPrice() * quantity.doubleValue();
	}

}
