package com.scrap.fitinpartscrapper.models;

public class ProductURL {

	private String URL;
	private Boolean scrapped;

	public ProductURL(String uRL, Boolean scrapped) {
		super();
		URL = uRL;
		this.scrapped = scrapped;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Boolean getScrapped() {
		return scrapped;
	}

	public void setScrapped(Boolean scrapped) {
		this.scrapped = scrapped;
	}

}
