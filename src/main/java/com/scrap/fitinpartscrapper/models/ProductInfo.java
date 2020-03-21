package com.scrap.fitinpartscrapper.models;

public class ProductInfo {

	private String name;
	private String partNumber;
	private String brand;
	private String imageURL;
	private String specifications;
	private String parentAssembly;
	private String description;
	private String vehicles;
	private String oeNumbers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public String getParentAssembly() {
		return parentAssembly;
	}

	public void setParentAssembly(String parentAssembly) {
		this.parentAssembly = parentAssembly;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVehicles() {
		return vehicles;
	}

	public void setVehicles(String vehicles) {
		this.vehicles = vehicles;
	}

	public String getOeNumbers() {
		return oeNumbers;
	}

	public void setOeNumbers(String oeNumbers) {
		this.oeNumbers = oeNumbers;
	}
}
