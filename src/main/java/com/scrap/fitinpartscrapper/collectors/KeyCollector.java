package com.scrap.fitinpartscrapper.collectors;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.scrap.fitinpartscrapper.models.Option;
import com.scrap.fitinpartscrapper.models.ProductInfo;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KeyCollector {

	private KeyCollector() {
		// utility class
	}

	/**
	 * The option regex
	 */
	private static final Pattern OPTION_REGEX = Pattern
			.compile("\\<option value\\=\\\\\\\"([\\w]+)\\\\\\\"\\>([^\\<\\\\\\/]+)\\<\\\\\\/option\\>");

	/**
	 * Parses the product brand
	 * 
	 * @param document The document
	 * @return The product brand
	 */
	private static String parseProductBrand(Document document) {

		Elements brandElements = document.getElementsByAttributeValue("itemprop", "brand");
		if (brandElements != null) {
			Element brandElement = brandElements.first();
			if (brandElement != null) {
				return brandElement.attr("content");
			}
		}

		return StringUtils.EMPTY;
	}

	/**
	 * Parses the product name
	 * 
	 * @param document The document
	 * @return The product name
	 */
	private static String parseProductName(Document document) {

		Elements brandElements = document.getElementsByAttributeValue("itemprop", "name");
		if (brandElements != null) {
			Element brandElement = brandElements.first();
			if (brandElement != null) {
				return brandElement.attr("content");
			}
		}

		return StringUtils.EMPTY;
	}

	/**
	 * Parses the product model
	 * 
	 * @param document The document
	 * @return The product model
	 */
	private static String parseProductModel(Document document) {

		Elements brandElements = document.getElementsByAttributeValue("itemprop", "model");
		if (brandElements != null) {
			Element brandElement = brandElements.first();
			if (brandElement != null) {
				return brandElement.attr("content");
			}
		}

		return StringUtils.EMPTY;
	}

	/**
	 * Gets the product image URL
	 * 
	 * @param document The document
	 * @return The product image URL
	 */
	private static String getProductImageURL(Document document) {

		Elements imageElements = document.getElementsByClass("thumbnail");
		if (imageElements != null) {
			Element imageElement = imageElements.first();
			if (imageElement != null) {
				return imageElement.attr("href");
			}
		}

		return StringUtils.EMPTY;
	}

	/**
	 * Gets the product Specifications
	 * 
	 * @param document The document
	 * @return The product specifications
	 */
	private static String getProductSpecifications(Document document) {

		Elements specElements = document.getElementsByClass("table-responsive");
		if (specElements != null) {
			Element specElement = specElements.first();
			if (specElement != null) {
				Elements rowElements = specElement.getElementsByTag("tr");
				ArrayList<String> rows = new ArrayList<>();
				if (rowElements != null) {
					ListIterator<Element> rowIterator = rowElements.listIterator();
					while (rowIterator.hasNext()) {
						Element rowElement = rowIterator.next();
						Elements colElements = rowElement.getElementsByTag("td");
						ArrayList<String> columns = new ArrayList<>();
						if (colElements != null && colElements.size() == 2) {
							String key = "";
							String value = "";
							Element firstElement = colElements.get(0);
							Elements boldElements = firstElement.getElementsByTag("b");
							if (boldElements != null) {
								Element boldElement = boldElements.first();
								if (boldElement != null) {
									key = boldElement.ownText();
								}
							}
							Element secondElement = colElements.get(1);
							if (secondElement != null) {
								value = secondElement.ownText();
							}

							if (StringUtils.isNoneBlank(key, value)) {
								columns.add(key);
								columns.add(value);
							}
							rows.add(StringUtils.join(columns, "|"));
						}
					}
				}
				return StringUtils.join(rows, "^");
			}
		}

		return StringUtils.EMPTY;
	}

	/**
	 * Gets the parent assembly
	 * 
	 * @param document The document
	 * @return The parent assembly
	 */
	private static String getParentAssembly(Document document) {

		Elements divElements = document.getElementsByClass("col-sm-5 col-xs-12 ");
		if (divElements != null) {
			Element divElement = divElements.first();
			if (divElement != null) {
				Elements tableElements = divElement.getElementsByTag("table");
				if (tableElements != null) {
					Element tableElement = tableElements.first();
					if (tableElement != null) {
						ArrayList<String> rows = new ArrayList<>();
						Elements trElements = tableElement.getElementsByTag("tr");
						if (trElements != null) {
							ArrayList<String> columns = new ArrayList<>();
							ListIterator<Element> rowIterator = trElements.listIterator();
							Element heading = rowIterator.next();
							if (heading != null) {
								Elements thElements = heading.getElementsByTag("th");
								ListIterator<Element> headingIterator = thElements.listIterator();

								while (headingIterator.hasNext()) {
									columns.add(headingIterator.next().ownText());
								}
								rows.add(StringUtils.join(columns, "|"));
							}

							while (rowIterator.hasNext()) {
								Element rowElement = rowIterator.next();
								Elements colElements = rowElement.getElementsByTag("td");
								columns = new ArrayList<>();
								if (colElements != null) {
									String brand = "";
									String part = "";
									String type = "";

									Element brandElement = colElements.get(0);
									if (brandElement != null) {
										Elements bElements = brandElement.getElementsByTag("b");
										if (bElements != null) {
											Element bElement = bElements.first();
											brand = bElement.ownText();
											columns.add(brand);
										}
									}

									Element partElement = colElements.get(1);
									if (partElement != null) {
										Elements aElements = partElement.getElementsByTag("a");
										if (aElements != null) {
											Element aElement = aElements.first();
											part = aElement.ownText();
											columns.add(part);
										}
									}

									Element typeElement = colElements.get(2);
									if (typeElement != null) {
										type = typeElement.ownText();
										columns.add(type);
									}

									rows.add(StringUtils.join(columns, "|"));
								}
							}

							return StringUtils.join(rows, "^");
						}
					}
				}
			}
		}
		return StringUtils.EMPTY;

	}

	/**
	 * Gets the product description
	 * 
	 * @param document The document
	 * @return The product description
	 */
	private static String getProductDescription(Document document) {

		Element descElement = document.getElementById("tab-description");
		if (descElement != null) {
			return StringUtils.join(descElement.text().replaceAll("\\*", ": ").split("\\."), "^");
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Gets the product OE numbers
	 * 
	 * @param document The document
	 * @return The product OE numbers
	 */
	private static String getProductOENumbers(Document document) {

		Elements tableElements = document.select("table:contains(OE Numbers)");
		if (tableElements != null) {
			Element tableElement = tableElements.first();
			if (tableElement != null) {
				Elements bodyElements = tableElement.getElementsByTag("tbody");
				if (bodyElements != null) {
					Element bodyElement = bodyElements.first();
					ArrayList<String> rows = new ArrayList<>();
					Elements trElements = bodyElement.getElementsByTag("tr");
					if (trElements != null) {
						ArrayList<String> columns = new ArrayList<>();
						ListIterator<Element> rowIterator = trElements.listIterator();
						Element heading = rowIterator.next();
						if (heading != null) {
							Elements thElements = heading.getElementsByTag("th");
							ListIterator<Element> headingIterator = thElements.listIterator();

							while (headingIterator.hasNext()) {
								columns.add(headingIterator.next().ownText());
							}
							rows.add(StringUtils.join(columns, "|"));
						}

						while (rowIterator.hasNext()) {
							Element rowElement = rowIterator.next();
							Elements colElements = rowElement.getElementsByTag("td");
							columns = new ArrayList<>();
							if (colElements != null) {
								ListIterator<Element> iterator = colElements.listIterator();

								while (iterator.hasNext()) {
									Elements anchorElements = iterator.next().getElementsByTag("a");
									if (anchorElements != null) {
										Element anchorElement = anchorElements.first();
										if (anchorElement != null) {
											columns.add(anchorElement.ownText());
										}
									}
								}
								rows.add(StringUtils.join(columns, "|"));
							}
						}

						return StringUtils.join(rows, "^");
					}
				}
			}
		}
		return StringUtils.EMPTY;
	}

	/**
	 * Gets the product vehicles
	 * 
	 * @param document The document
	 * @return The product verhicles
	 */
	private static String getProductVehicles(Document document) {

		Element specElement = document.getElementById("tab-specification");
		if (specElement != null) {
			ArrayList<String> rows = new ArrayList<>();
			rows.add("Brand|Model|Placement|Production|Eng.Vol.|Body No|Eng No|Notes");
			Elements panelElements = specElement.getElementsByClass("panel panel-default");
			if (panelElements != null) {
				ListIterator<Element> panelIterator = panelElements.listIterator();
				while (panelIterator.hasNext()) {
					Element panelElement = panelIterator.next();
					if (panelElement != null) {
						Elements divElements = panelElement.getElementsByClass("row");
						Elements tableElements = panelElement.select("tr:has(td)");
						ListIterator<Element> divIterator = divElements.listIterator();
						ListIterator<Element> tableIterator = tableElements.listIterator();

						if (!tableElements.isEmpty()) {
							while (divIterator.hasNext()) {
								String divHTML = divIterator.next().text();
								Elements tdElements = tableIterator.next().getElementsByTag("td");
								if (tdElements != null) {
									ArrayList<String> columns = new ArrayList<>();
									columns.add(divHTML);
									ListIterator<Element> tdIterator = tdElements.listIterator();
									while (tdIterator.hasNext()) {
										columns.add(tdIterator.next().ownText());
									}
									rows.add(StringUtils.join(columns, "|"));
								}
							}
						}

					}
				}
				return StringUtils.join(rows, "^");
			}
		}
		return StringUtils.EMPTY;

	}

	/**
	 * Gets the list of brands
	 * 
	 * @throws IOException
	 */
	public static List<Option> getBrands() throws IOException {

		List<Option> brands = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getBrand")
				.post(RequestBody.create(new byte[0])).addHeader("Cache-Control", "no-cache").build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				brands.add(new Option(id.trim(), name.trim()));
			}
		}

		return brands;
	}

	/**
	 * Gets the list of models for the specified brand
	 * 
	 * @param brandId The brand ID
	 * @return The list of models
	 * @throws IOException
	 */
	public static List<Option> getModels(String brandId) throws IOException {

		List<Option> models = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("class=0&brand=" + brandId + "&veh_type=1", mediaType);
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getModel").post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				models.add(new Option(id.trim(), name.trim()));
			}
		}

		return models;
	}

	/**
	 * Gets the available years for the specified model
	 * 
	 * @param modelId The model id
	 * @return The list of years
	 * @throws IOException
	 */
	public static List<Option> getYears(String modelId) throws IOException {

		List<Option> years = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("model=" + modelId, mediaType);
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getYears").post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				years.add(new Option(id.trim(), name.trim()));
			}
		}

		return years;
	}

	/**
	 * Gets the available bodies for the specified model
	 * 
	 * @param modelId The model id
	 * @return The list of bodies
	 * @throws IOException
	 */
	public static List<Option> getBodies(String modelId) throws IOException {

		List<Option> bodies = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("model=" + modelId, mediaType);
		Request request = new Request.Builder().url("https://www.fitinpart.sg/index.php?route=module/appsearch/getBody")
				.post(body).addHeader("Content-Type", "application/x-www-form-urlencoded")
				.addHeader("Cache-Control", "no-cache").build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				bodies.add(new Option(id.trim(), name.trim()));
			}
		}

		return bodies;
	}

	/**
	 * Gets the available engines for the specified model and body
	 * 
	 * @param modelId The model id
	 * @param bodyId  the body id
	 * @return The list of engines
	 * @throws IOException
	 */
	public static List<Option> getEngines(String modelId, String bodyId) throws IOException {

		List<Option> engines = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("model=" + modelId + "&body=" + bodyId, mediaType);
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getEngine").post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				engines.add(new Option(id.trim(), name.trim()));
			}
		}

		return engines;
	}

	/**
	 * Gets the available years for the specified model
	 * 
	 * @param modelId    The model id
	 * @param bodyName   The body id
	 * @param engineName The engine id
	 * @return The list of engine types
	 * @throws IOException
	 */
	public static List<Option> getEngineTypes(String modelId, String bodyId, String engineId) throws IOException {

		List<Option> engineTypes = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
		RequestBody body = RequestBody.create("model=" + modelId + "&body=" + bodyId + "&engine=" + engineId,
				mediaType);
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getEngineNo").post(body)
				.addHeader("Content-Type", "application/x-www-form-urlencoded").addHeader("Cache-Control", "no-cache")
				.build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Matcher optionMatcher = OPTION_REGEX.matcher(data);

		while (optionMatcher.find()) {
			String id = optionMatcher.group(1);
			String name = optionMatcher.group(2);

			if (StringUtils.isNoneBlank(id, name)) {
				engineTypes.add(new Option(id.trim(), name.trim()));
			}
		}

		return engineTypes;
	}

	/**
	 * Gets the list of categories
	 * 
	 * @param brandId      The brand ID
	 * @param modelId      The model ID
	 * @param yearId       The year ID
	 * @param bodyId       The body ID
	 * @param engineId     The engine ID
	 * @param engineTypeId The engine type ID
	 * @return The list of categories
	 * @throws IOException
	 */
	public static List<Option> getCategories(String brandId, String modelId, String yearId, String bodyId,
			String engineId, String engineTypeId) throws IOException {

		List<Option> categories = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId + "&m="
						+ modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n=" + engineTypeId
						+ "&show=1&init_search=1")
				.get().addHeader("Cache-Control", "no-cache").build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Document document = Jsoup.parse(data);
		Elements elements = document.getElementsByClass("filter_tabs");
		if (elements != null) {
			Element element = elements.first();
			Elements anchors = element.getElementsByTag("a");
			if (anchors != null) {
				ListIterator<Element> anchorElements = anchors.listIterator();
				while (anchorElements.hasNext()) {
					Element anchorElement = anchorElements.next();
					String id = anchorElement.attr("data-category_id");
					String name = anchorElement.ownText();

					if (StringUtils.isNoneBlank(id, name) && !"0".equals(id.trim())) {
						categories.add(new Option(id.trim(), name.trim()));
					}
				}
			}
		}

		return categories;
	}

	/**
	 * Gets the list of sub-categories
	 * 
	 * @param brandId      The brand ID
	 * @param modelId      The model ID
	 * @param yearId       The year ID
	 * @param bodyId       The body ID
	 * @param engineId     The engine ID
	 * @param engineTypeId The engine type ID
	 * @param categoryId   The category ID
	 * @return The list of categories
	 * @throws IOException
	 */
	public static List<Option> getSubCategories(String brandId, String modelId, String yearId, String bodyId,
			String engineId, String engineTypeId, String categoryId) throws IOException {

		List<Option> subCategories = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId + "&m="
						+ modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n=" + engineTypeId
						+ "&cat=" + categoryId + "&show=1&init_search=1")
				.get().addHeader("Cache-Control", "no-cache").build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Document document = Jsoup.parse(data);
		Elements elements = document.getElementsByClass("filter_types");
		if (elements != null) {
			Element element = elements.first();
			Elements labels = element.getElementsByTag("label");
			if (labels != null) {
				ListIterator<Element> labelElements = labels.listIterator();
				while (labelElements.hasNext()) {
					Element label = labelElements.next();
					Elements inputElements = label.getElementsByTag("input");
					if (inputElements != null) {
						Element inputElement = inputElements.first();
						String id = inputElement.attr("value");
						String name = label.ownText();

						if (StringUtils.isNoneBlank(id, name)) {
							subCategories.add(new Option(id.trim(), name.trim()));
						}
					}
				}
			}
		}

		return subCategories;
	}

	/**
	 * Gets the list of product links
	 * 
	 * @param brandId       The brand ID
	 * @param modelId       The model ID
	 * @param yearId        The year ID
	 * @param bodyId        The body ID
	 * @param engineId      The engine ID
	 * @param engineTypeId  The engine type ID
	 * @param categoryId    The category ID
	 * @param subcategoryId The subcategory ID
	 * @return The list of categories
	 * @throws IOException
	 */
	public static List<String> getProductsLinks(String brandId, String modelId, String yearId, String bodyId,
			String engineId, String engineTypeId, String categoryId, String subcategoryId) throws IOException {

		List<String> links = new ArrayList<>();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId + "&m="
						+ modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n=" + engineTypeId
						+ "&cat=" + categoryId + "&p_type=" + subcategoryId + "&show=1&init_search=1")
				.get().addHeader("Cache-Control", "no-cache").build();

		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Document document = Jsoup.parse(data);
		Element element = document.getElementById("app_products");
		if (element != null) {
			Elements headings = element.getElementsByClass("pro_box_title");
			if (headings != null) {
				ListIterator<Element> headingIterator = headings.listIterator();
				while (headingIterator.hasNext()) {
					Element heading = headingIterator.next();
					Elements anchors = heading.getElementsByTag("a");
					if (anchors != null) {
						Element anchor = anchors.first();
						String url = anchor.attr("href");

						if (StringUtils.isNotBlank(url)) {
							links.add(URLDecoder.decode(url, "UTF-8"));
						}
					}
				}
			}
		}

		return links;
	}

	/**
	 * Gets the product info
	 * 
	 * @param productURL The product URL
	 * @return The product info
	 * @throws IOException
	 */
	public static ProductInfo getProductInfo(String productURL) throws IOException {

		ProductInfo productInfo = new ProductInfo();
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(productURL).get().addHeader("Cache-Control", "no-cache").build();
		Response response = client.newCall(request).execute();
		String data = response.body().string();
		Document document = Jsoup.parse(data);

		productInfo.setBrand(parseProductBrand(document));
		productInfo.setPartNumber(parseProductModel(document));
		productInfo.setName(parseProductName(document));
		productInfo.setImageURL(getProductImageURL(document));
		productInfo.setSpecifications(getProductSpecifications(document));
		productInfo.setParentAssembly(getParentAssembly(document));
		productInfo.setDescription(getProductDescription(document));
		productInfo.setOeNumbers(getProductOENumbers(document));
		productInfo.setVehicles(getProductVehicles(document));
		return productInfo;
	}
}
