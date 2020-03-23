package com.scrap.fitinpartscrapper.models;

import java.util.List;

public class Product {

	private String brand;
	private String model;
	private String year;
	private String body;
	private String engine;
	private String engineType;
	private String category;
	private String subCategory;
	private List<ProductURL> productURLs;

	public Product(String brand, String model, String year, String body, String engine, String engineType,
			String category, String subCategory, List<ProductURL> productURLs) {
		super();
		this.brand = brand;
		this.model = model;
		this.year = year;
		this.body = body;
		this.engine = engine;
		this.engineType = engineType;
		this.category = category;
		this.subCategory = subCategory;
		this.productURLs = productURLs;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public List<ProductURL> getProductURLs() {
		return productURLs;
	}

	public void setProductURLs(List<ProductURL> productURLs) {
		this.productURLs = productURLs;
	}
}
