package com.scrap.fitinpartscrapper.collectors;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.scrap.fitinpartscrapper.models.Option;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@Service
public class KeyCollector {

	private KeyCollector() {
		// utility class
	}

	/**
	 * The option regex
	 */
	private final Pattern OPTION_REGEX = Pattern
			.compile("\\<option value\\=\\\\\\\"([\\w]+)\\\\\\\"\\>([^\\<\\\\\\/]+)\\<\\\\\\/option\\>");

	/**
	 * The maximum number of retries
	 */
	private final Integer MAX_RETRIES = 3;

	/**
	 * Gets the list of brands
	 */
	public List<Option> getBrands() {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
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
			} catch (Exception exception) {
				log.error("Error fetching the brands. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}

	/**
	 * Gets the list of models for the specified brand
	 * 
	 * @param brandId The brand ID
	 * @return The list of models
	 */
	public List<Option> getModels(String brandId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> models = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				RequestBody body = RequestBody.create("class=0&brand=" + brandId + "&veh_type=1", mediaType);
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getModel").post(body)
						.addHeader("Content-Type", "application/x-www-form-urlencoded")
						.addHeader("Cache-Control", "no-cache").build();

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
			} catch (Exception exception) {
				log.error("Error fetching the models. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}

	/**
	 * Gets the available years for the specified model
	 * 
	 * @param modelId The model id
	 * @return The list of years
	 */
	public List<Option> getYears(String modelId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> years = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				RequestBody body = RequestBody.create("model=" + modelId, mediaType);
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getYears").post(body)
						.addHeader("Content-Type", "application/x-www-form-urlencoded")
						.addHeader("Cache-Control", "no-cache").build();

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
			} catch (Exception exception) {
				log.error("Error fetching the years. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}

	/**
	 * Gets the available bodies for the specified model
	 * 
	 * @param modelId The model id
	 * @return The list of bodies
	 */
	public List<Option> getBodies(String modelId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> bodies = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				RequestBody body = RequestBody.create("model=" + modelId, mediaType);
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getBody").post(body)
						.addHeader("Content-Type", "application/x-www-form-urlencoded")
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
			} catch (Exception exception) {
				log.error("Error fetching the bodies. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}

	/**
	 * Gets the available engines for the specified model and body
	 * 
	 * @param modelId The model id
	 * @param bodyId  the body id
	 * @return The list of engines
	 */
	public List<Option> getEngines(String modelId, String bodyId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> engines = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				RequestBody body = RequestBody.create("model=" + modelId + "&body=" + bodyId, mediaType);
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getEngine").post(body)
						.addHeader("Content-Type", "application/x-www-form-urlencoded")
						.addHeader("Cache-Control", "no-cache").build();

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
			} catch (Exception exception) {
				log.error("Error fetching the engines. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}

	/**
	 * Gets the available years for the specified model
	 * 
	 * @param modelId    The model id
	 * @param bodyName   The body id
	 * @param engineName The engine id
	 * @return The list of engine types
	 */
	public List<Option> getEngineTypes(String modelId, String bodyId, String engineId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> engineTypes = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				RequestBody body = RequestBody.create("model=" + modelId + "&body=" + bodyId + "&engine=" + engineId,
						mediaType);
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=module/appsearch/getEngineNo").post(body)
						.addHeader("Content-Type", "application/x-www-form-urlencoded")
						.addHeader("Cache-Control", "no-cache").build();

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
			} catch (Exception exception) {
				log.error("Error fetching the engine types. Retrying...", exception);
			}
		}
		return Arrays.asList();
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
	 */
	public List<Option> getCategories(String brandId, String modelId, String yearId, String bodyId, String engineId,
			String engineTypeId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> categories = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId
								+ "&m=" + modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n="
								+ engineTypeId + "&show=1&init_search=1")
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
			} catch (Exception exception) {
				log.error("Error fetching the categories. Retrying...", exception);
			}
		}
		return Arrays.asList();
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
	 */
	public List<Option> getSubCategories(String brandId, String modelId, String yearId, String bodyId, String engineId,
			String engineTypeId, String categoryId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<Option> subCategories = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId
								+ "&m=" + modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n="
								+ engineTypeId + "&cat=" + categoryId + "&show=1&init_search=1")
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
			} catch (Exception exception) {
				log.error("Error fetching the sub-categories. Retrying...", exception);
			}
		}
		return Arrays.asList();
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
	 */
	public List<String> getProductsLinks(String brandId, String modelId, String yearId, String bodyId, String engineId,
			String engineTypeId, String categoryId, String subcategoryId) {

		for (int i = 0; i < MAX_RETRIES; i++) {
			try {
				List<String> links = new ArrayList<>();
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("https://www.fitinpart.sg/index.php?route=product/category/appSearch&veh=1&b=" + brandId
								+ "&m=" + modelId + "&y=" + yearId + "&b_v=" + bodyId + "&e_v=" + engineId + "&e_n="
								+ engineTypeId + "&cat=" + categoryId + "&p_type=" + subcategoryId
								+ "&show=1&init_search=1")
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
			} catch (Exception exception) {
				log.error("Error fetching the product links. Retrying...", exception);
			}
		}
		return Arrays.asList();
	}
}
