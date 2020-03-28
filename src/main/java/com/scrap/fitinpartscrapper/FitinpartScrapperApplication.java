package com.scrap.fitinpartscrapper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.Option;

@SpringBootApplication
public class FitinpartScrapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitinpartScrapperApplication.class, args);

		try (FileOutputStream logFileOutputStream = new FileOutputStream("log.txt");
				FileOutputStream dataOutputStream = new FileOutputStream("data.csv")) {
			List<Option> brands = KeyCollector.getBrands();

			for (Option brand : brands) {
				logFileOutputStream.write(String.format("Brand: %s\n", brand.getName()).getBytes());
				List<Option> models = KeyCollector.getModels(brand.getId());

				for (Option model : models) {
					logFileOutputStream.write(
							String.format("Brand: %s -> Model: %s\n", brand.getName(), model.getName()).getBytes());
					List<Option> years = KeyCollector.getYears(model.getId());

					for (Option year : years) {
						logFileOutputStream.write(String.format("Brand: %s -> Model: %s -> Year: %s\n", brand.getName(),
								model.getName(), year.getName()).getBytes());
						List<Option> bodies = KeyCollector.getBodies(model.getId());

						for (Option body : bodies) {
							logFileOutputStream.write(String.format("Brand: %s -> Model: %s -> Year: %s -> Body: %s\n",
									brand.getName(), model.getName(), year.getName(), body.getName()).getBytes());
							List<Option> engines = KeyCollector.getEngines(model.getId(), body.getId());

							for (Option engine : engines) {
								logFileOutputStream.write(
										String.format("Brand: %s -> Model: %s -> Year: %s -> Body: %s -> Engine: %s\n",
												brand.getName(), model.getName(), year.getName(), body.getName(),
												engine.getName()).getBytes());
								List<Option> engineTypes = KeyCollector.getEngineTypes(model.getId(), body.getId(),
										engine.getId());

								for (Option engineType : engineTypes) {
									dataOutputStream.write((StringUtils
											.join(new String[] { brand.getName(), model.getName(), year.getName(),
													body.getName(), engine.getName(), engineType.getName() }, ",")
											+ "\n").getBytes());
								}
							}
						}
					}
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
