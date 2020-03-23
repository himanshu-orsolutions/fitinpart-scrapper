package com.scrap.fitinpartscrapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.GsonBuilder;
import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.Container;
import com.scrap.fitinpartscrapper.models.Option;
import com.scrap.fitinpartscrapper.models.Product;
import com.scrap.fitinpartscrapper.models.ProductURL;

@SpringBootApplication
public class FitinpartScrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitinpartScrapperApplication.class, args);

		try (FileOutputStream logFileOutputStream = new FileOutputStream("log.txt")) {
			List<Product> products = new ArrayList<>();
			try {

				logFileOutputStream.write("Fetching Brands...\n".getBytes());
				List<Option> brands = KeyCollector.getBrands();
				logFileOutputStream.write("Brands fetched successfully. :)\n".getBytes());

				for (Option brand : brands) {
					logFileOutputStream.write(("Fetching models for brand: " + brand.getName() + "\n").getBytes());
					List<Option> models = KeyCollector.getModels(brand.getId());
					logFileOutputStream.write("Models fetched successfully. :)\n".getBytes());

					for (Option model : models) {
						logFileOutputStream.write(("Fetching years for model: " + model.getName() + "\n").getBytes());
						List<Option> years = KeyCollector.getYears(model.getId());
						logFileOutputStream.write("Years fetched successfully. :)\n".getBytes());

						for (Option year : years) {
							logFileOutputStream
									.write(("Fetching bodies for model: " + model.getName() + "\n").getBytes());
							List<Option> bodies = KeyCollector.getBodies(model.getId());
							logFileOutputStream.write("Bodies fetched successfully. :)\n".getBytes());

							for (Option body : bodies) {
								logFileOutputStream.write(("Fetching engines for model: " + model.getName()
										+ " and body: " + body.getName() + "\n").getBytes());
								List<Option> engines = KeyCollector.getEngines(model.getId(), body.getId());
								logFileOutputStream.write("Engines fetched successfully. :)\n".getBytes());

								for (Option engine : engines) {
									logFileOutputStream.write(String
											.format("Fetching engine types for model: %s, body: %s, and engine: %s\n",
													model.getName(), body.getName(), engine.getName())
											.getBytes());
									List<Option> engineTypes = KeyCollector.getEngineTypes(model.getId(), body.getId(),
											engine.getId());
									logFileOutputStream.write("Engine types fetched successfully. :)\n".getBytes());

									for (Option engineType : engineTypes) {
										logFileOutputStream.write(String.format(
												"Fetching categories for model: %s, body: %s, engine: %s, and engine type: %s\n",
												model.getName(), body.getName(), engine.getName(), engineType.getName())
												.getBytes());
										List<Option> categories = KeyCollector.getCategories(brand.getId(),
												model.getId(), year.getId(), body.getId(), engine.getId(),
												engineType.getId());
										logFileOutputStream.write("Cetegories fetched successfully. :)\n".getBytes());

										for (Option category : categories) {
											logFileOutputStream.write(String.format(
													"Fetching sub-categories for model: %s, body: %s, engine: %s, engine type: %s, and category: %s\n",
													model.getName(), body.getName(), engine.getName(),
													engineType.getName(), category.getName()).getBytes());
											List<Option> subCategories = KeyCollector.getSubCategories(brand.getId(),
													model.getId(), year.getId(), body.getId(), engine.getId(),
													engineType.getId(), category.getId());
											logFileOutputStream
													.write("Sub-cetegories fetched successfully. :)\n".getBytes());

											for (Option subCategory : subCategories) {

												logFileOutputStream.write(String.format(
														"Fetching product URLs for model: %s, body: %s, engine: %s, engine type: %s, category: %s, and sub-category: %s\n",
														model.getName(), body.getName(), engine.getName(),
														engineType.getName(), category.getName(), subCategory.getName())
														.getBytes());
												List<String> urls = KeyCollector.getProductsLinks(brand.getId(),
														model.getId(), year.getId(), body.getId(), engine.getId(),
														engineType.getId(), category.getId(), subCategory.getId());
												logFileOutputStream
														.write("Product URLs fetched successfully. :)\n".getBytes());

												List<ProductURL> productURLs = new ArrayList<>();
												urls.forEach(url -> productURLs.add(new ProductURL(url, false)));

												products.add(new Product(brand.getName(), model.getName(),
														year.getName(), body.getName(), engine.getName(),
														engineType.getName(), category.getName(), subCategory.getName(),
														productURLs));
											}
										}
									}
								}
							}
						}
					}
				}
			} catch (IOException ioException) {
				logFileOutputStream.write(ioException.getMessage().getBytes());
			}

			try (FileOutputStream outputStream = new FileOutputStream("products.json")) {
				outputStream.write(
						new GsonBuilder().setPrettyPrinting().create().toJson(new Container(products)).getBytes());
				outputStream.flush();
			} catch (IOException ioException) {
				logFileOutputStream.write(ioException.getMessage().getBytes());
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
