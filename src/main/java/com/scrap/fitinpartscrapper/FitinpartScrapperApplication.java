package com.scrap.fitinpartscrapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.scrap.fitinpartscrapper.collectors.ImageDownloader;
import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.Option;
import com.scrap.fitinpartscrapper.models.ProductInfo;

@SpringBootApplication
public class FitinpartScrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitinpartScrapperApplication.class, args);
		
		if (!Files.exists(Paths.get("images"))) {
			try {
				Files.createDirectory(Paths.get("images"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("result.csv"))) {
			bufferedWriter.write(
					"\"car\",\"model\",\"year\",\"body\",\"engine\",\"engine_number\",\"category\",\"sub_category\",\"product_url\",product_name\",\"brand\",\"part_number\",\"image_url\",\"specifications\",\"parent_assembly\",\"description\",\"vehicles\",\"oe_numbers\",\"image_path\"");
			bufferedWriter.newLine();

			System.out.println("Fetching Brands...");
			List<Option> brands = KeyCollector.getBrands();
			System.out.println("Brands fetched successfully. :)");

			for (Option brand : brands) {
				System.out.println("Fetching models for brand: " + brand.getName());
				List<Option> models = KeyCollector.getModels(brand.getId());
				System.out.println("Models fetched successfully. :)");

				for (Option model : models) {
					System.out.println("Fetching years for model: " + model.getName());
					List<Option> years = KeyCollector.getYears(model.getId());
					System.out.println("Years fetched successfully. :)");

					for (Option year : years) {
						System.out.println("Fetching bodies for model: " + model.getName());
						List<Option> bodies = KeyCollector.getBodies(model.getId());
						System.out.println("Bodies fetched successfully. :)");

						for (Option body : bodies) {
							System.out.println(
									"Fetching engines for model: " + model.getName() + " and body: " + body.getName());
							List<Option> engines = KeyCollector.getEngines(model.getId(), body.getId());
							System.out.println("Engines fetched successfully. :)");

							for (Option engine : engines) {
								System.out.println(
										String.format("Fetching engine types for model: %s, body: %s, and engine: %s",
												model.getName(), body.getName(), engine.getName()));
								List<Option> engineTypes = KeyCollector.getEngineTypes(model.getId(), body.getId(),
										engine.getId());
								System.out.println("Engine types fetched successfully. :)");

								for (Option engineType : engineTypes) {
									System.out.println(String.format(
											"Fetching categories for model: %s, body: %s, engine: %s, and engine type: %s",
											model.getName(), body.getName(), engine.getName(), engineType.getName()));
									List<Option> categories = KeyCollector.getCategories(brand.getId(), model.getId(),
											year.getId(), body.getId(), engine.getId(), engineType.getId());
									System.out.println("Cetegories fetched successfully. :)");

									for (Option category : categories) {
										System.out.println(String.format(
												"Fetching sub-categories for model: %s, body: %s, engine: %s, engine type: %s, and category: %s",
												model.getName(), body.getName(), engine.getName(), engineType.getName(),
												category.getName()));
										List<Option> subCategories = KeyCollector.getSubCategories(brand.getId(),
												model.getId(), year.getId(), body.getId(), engine.getId(),
												engineType.getId(), category.getId());
										System.out.println("Sub-cetegories fetched successfully. :)");

										for (Option subCategory : subCategories) {

											System.out.println(String.format(
													"Fetching product URLs for model: %s, body: %s, engine: %s, engine type: %s, category: %s, and sub-category: %s",
													model.getName(), body.getName(), engine.getName(),
													engineType.getName(), category.getName(), subCategory.getName()));
											List<String> productURLs = KeyCollector.getProductsLinks(brand.getId(),
													model.getId(), year.getId(), body.getId(), engine.getId(),
													engineType.getId(), category.getId(), subCategory.getId());
											System.out.println("Product URLs fetched successfully. :)");

											for (String productURL : productURLs) {
												System.out.println(
														"Fetching the product information from URL: " + productURL);
												try {
													ProductInfo productInfo = KeyCollector.getProductInfo(productURL);
													System.out.println("Information fetched successfully :)");

													String imagePath = "images/"
															+ StringUtils
																	.join(Arrays.asList(year.getName(), brand.getName(),
																			model.getName(), productInfo.getBrand(),
																			productInfo.getPartNumber()), "-")
															+ "." + productInfo.getImageURL().substring(
																	productInfo.getImageURL().lastIndexOf(".") + 1);

													bufferedWriter.write(StringUtils.join(Arrays.asList(
															"\"" + brand.getName() + "\"",
															"\"" + model.getName() + "\"", "\"" + year.getName() + "\"",
															"\"" + body.getName() + "\"",
															"\"" + engine.getName() + "\"",
															"\"" + engineType.getName() + "\"",
															"\"" + category.getName() + "\"",
															"\"" + subCategory.getName() + "\"",
															"\"" + productURL + "\"",
															"\"" + productInfo.getName() + "\"",
															"\"" + productInfo.getBrand() + "\"",
															"\"" + productInfo.getPartNumber() + "\"",
															"\"" + productInfo.getImageURL() + "\"",
															"\"" + productInfo.getSpecifications() + "\"",
															"\"" + productInfo.getParentAssembly() + "\"",
															"\"" + productInfo.getDescription() + "\"",
															"\"" + productInfo.getVehicles() + "\"",
															"\"" + productInfo.getOeNumbers() + "\"",
															"\"" + (StringUtils.isNotBlank(productInfo.getImageURL())
																	? imagePath
																	: "NA") + "\""),
															","));
													bufferedWriter.newLine();

													// Downloading the image
													try {
														if (StringUtils.isNotBlank(productInfo.getImageURL())) {
															System.out
																	.println("Downloading the product image from URL: "
																			+ productInfo.getImageURL());
															ImageDownloader.downloadImage(productInfo.getImageURL(),
																	Paths.get(imagePath));
														}
													} catch (IOException ioException) {
														System.out.println("Error downloading the image from URL: "
																+ productInfo.getImageURL());
													}
												} catch (IOException ioException) {
													System.out.println("Error getting information of product with URL: "
															+ productURL);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException ioException) {
			System.out.println("Error getting the keys.");
			ioException.printStackTrace();
		}
	}
}
