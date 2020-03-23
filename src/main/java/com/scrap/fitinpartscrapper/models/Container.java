package com.scrap.fitinpartscrapper.models;

import java.util.List;

public class Container {

	List<Product> products;

	public Container(List<Product> products) {
		super();
		this.products = products;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
