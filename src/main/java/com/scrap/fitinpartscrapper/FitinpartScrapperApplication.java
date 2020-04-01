package com.scrap.fitinpartscrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.Gson;
import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.FitInPart;
import com.scrap.fitinpartscrapper.models.FitInPartInfo;
import com.scrap.fitinpartscrapper.models.FitInPartTag;
import com.scrap.fitinpartscrapper.models.Option;
import com.scrap.fitinpartscrapper.models.Parameters;
import com.scrap.fitinpartscrapper.services.FitInPartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class FitinpartScrapperApplication {

	@Autowired
	private FitInPartService fitInPartService;

	@Autowired
	private KeyCollector keyCollector;

	private ExecutorService taskExecutor = Executors.newCachedThreadPool();

	private void getModels(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format("Getting models for brand: %s", fitInPartInfo.getBrandName()));
		List<Option> models = this.keyCollector.getModels(parameters.getBrandId());

		if (models != null) {
			if (!models.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				models.forEach(model -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(model.getName()).build()))
						.parameters(new Gson().toJson(
								Parameters.builder().brandId(parameters.getBrandId()).modelId(model.getId()).build()))
						.tag(FitInPartTag.MODEL).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format("No models found for brand: %s. Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getYears(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format("Getting years for brand: %s, model: %s", fitInPartInfo.getBrandName(),
				fitInPartInfo.getModelName()));
		List<Option> years = this.keyCollector.getYears(parameters.getModelId());

		if (years != null) {
			if (!years.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				years.forEach(year -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(year.getName()).build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(parameters.getBrandId())
								.modelId(parameters.getModelId()).yearId(year.getId()).build()))
						.tag(FitInPartTag.YEAR).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No years found for brand: %s, model: %s. Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getBodies(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format("Getting bodies for brand: %s, model: %s, year: %s", fitInPartInfo.getBrandName(),
				fitInPartInfo.getModelName(), fitInPartInfo.getYearName()));
		List<Option> bodies = this.keyCollector.getBodies(parameters.getModelId());

		if (bodies != null) {
			if (!bodies.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				bodies.forEach(body -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(body.getName()).build()))
						.parameters(new Gson().toJson(
								Parameters.builder().brandId(parameters.getBrandId()).modelId(parameters.getModelId())
										.yearId(parameters.getYearId()).bodyId(body.getId()).build()))
						.tag(FitInPartTag.BODY).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No bodies found for brand: %s, model: %s, year: %s. Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getEngines(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format("Getting engines for brand: %s, model: %s, year: %s, body: %s",
				fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
				fitInPartInfo.getBodyName()));
		List<Option> engines = this.keyCollector.getEngines(parameters.getModelId(), parameters.getBodyId());

		if (engines != null) {
			if (!engines.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				engines.forEach(engine -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(fitInPartInfo.getBodyName()).engineName(engine.getName()).build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(parameters.getBrandId())
								.modelId(parameters.getModelId()).yearId(parameters.getYearId())
								.bodyId(parameters.getBodyId()).engineId(engine.getId()).build()))
						.tag(FitInPartTag.ENGINE).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No engines found for brand: %s, model: %s, year: %s, body: %s . Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
						fitInPartInfo.getBodyName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getEngineTypes(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format("Getting engine types for brand: %s, model: %s, year: %s, body: %s, engine: %s",
				fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
				fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName()));
		List<Option> engineTypes = this.keyCollector.getEngineTypes(parameters.getModelId(), parameters.getBodyId(),
				parameters.getEngineId());

		if (engineTypes != null) {
			if (!engineTypes.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				engineTypes.forEach(engineType -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(fitInPartInfo.getBodyName()).engineName(fitInPartInfo.getEngineName())
								.engineTypeName(engineType.getName()).build()))
						.parameters(new Gson().toJson(
								Parameters.builder().brandId(parameters.getBrandId()).modelId(parameters.getModelId())
										.yearId(parameters.getYearId()).bodyId(parameters.getBodyId())
										.engineId(parameters.getEngineId()).engineTypeId(engineType.getId()).build()))
						.tag(FitInPartTag.ENGINE_TYPE).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No engine types found for brand: %s, model: %s, year: %s, body: %s, engine: %s . Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
						fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getCategories(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format(
				"Getting categories for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s",
				fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
				fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName()));
		List<Option> categories = this.keyCollector.getCategories(parameters.getBrandId(), parameters.getModelId(),
				parameters.getYearId(), parameters.getBodyId(), parameters.getEngineId(), parameters.getEngineTypeId());

		if (categories != null) {
			if (!categories.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				categories.forEach(category -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(fitInPartInfo.getBodyName()).engineName(fitInPartInfo.getEngineName())
								.engineTypeName(fitInPartInfo.getEngineTypeName()).categoryName(category.getName())
								.build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(parameters.getBrandId())
								.modelId(parameters.getModelId()).yearId(parameters.getYearId())
								.bodyId(parameters.getBodyId()).engineId(parameters.getEngineId())
								.engineTypeId(parameters.getEngineTypeId()).categoryId(category.getId()).build()))
						.tag(FitInPartTag.CATEGORY).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No categories found for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s . Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
						fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getSubCategories(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format(
				"Getting sub-categories for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s, category: %s",
				fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
				fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName(),
				fitInPartInfo.getCategoryName()));
		List<Option> subCategories = this.keyCollector.getSubCategories(parameters.getBrandId(),
				parameters.getModelId(), parameters.getYearId(), parameters.getBodyId(), parameters.getEngineId(),
				parameters.getEngineTypeId(), parameters.getCategoryId());

		if (subCategories != null) {
			if (!subCategories.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				subCategories.forEach(subCategory -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(fitInPartInfo.getBodyName()).engineName(fitInPartInfo.getEngineName())
								.engineTypeName(fitInPartInfo.getEngineTypeName())
								.categoryName(fitInPartInfo.getCategoryName()).subCategoryName(subCategory.getName())
								.build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(parameters.getBrandId())
								.modelId(parameters.getModelId()).yearId(parameters.getYearId())
								.bodyId(parameters.getBodyId()).engineId(parameters.getEngineId())
								.engineTypeId(parameters.getEngineTypeId()).categoryId(parameters.getCategoryId())
								.subcategoryId(subCategory.getId()).build()))
						.tag(FitInPartTag.SUB_CATEGORY).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No sub-categories found for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s, category: %s . Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
						fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName(),
						fitInPartInfo.getCategoryName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	private void getProductLinks(Parameters parameters, FitInPartInfo fitInPartInfo, Long parentFitInPartId) {

		log.debug(String.format(
				"Getting product links for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s, category: %s, sub-category: %s",
				fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
				fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName(),
				fitInPartInfo.getCategoryName(), fitInPartInfo.getSubCategoryName()));
		List<String> productLinks = this.keyCollector.getProductsLinks(parameters.getBrandId(), parameters.getModelId(),
				parameters.getYearId(), parameters.getBodyId(), parameters.getEngineId(), parameters.getEngineTypeId(),
				parameters.getCategoryId(), parameters.getSubcategoryId());

		if (productLinks != null) {
			if (!productLinks.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				productLinks.forEach(productLink -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(fitInPartInfo.getBrandName())
								.modelName(fitInPartInfo.getModelName()).yearName(fitInPartInfo.getYearName())
								.bodyName(fitInPartInfo.getBodyName()).engineName(fitInPartInfo.getEngineName())
								.engineTypeName(fitInPartInfo.getEngineTypeName())
								.categoryName(fitInPartInfo.getCategoryName())
								.subCategoryName(fitInPartInfo.getSubCategoryName()).productURL(productLink).build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(parameters.getBrandId())
								.modelId(parameters.getModelId()).yearId(parameters.getYearId())
								.bodyId(parameters.getBodyId()).engineId(parameters.getEngineId())
								.engineTypeId(parameters.getEngineTypeId()).categoryId(parameters.getCategoryId())
								.subcategoryId(parameters.getSubcategoryId()).build()))
						.tag(FitInPartTag.PRODUCT).isScrapped(false).build()));

				this.fitInPartService.saveFitInParts(fitInParts, parentFitInPartId);

			} else {
				log.error(String.format(
						"No product links found for brand: %s, model: %s, year: %s, body: %s, engine: %s, engine type: %s, category: %s, sub-category: %s . Check the API calls before proceeding further.",
						fitInPartInfo.getBrandName(), fitInPartInfo.getModelName(), fitInPartInfo.getYearName(),
						fitInPartInfo.getBodyName(), fitInPartInfo.getEngineName(), fitInPartInfo.getEngineTypeName(),
						fitInPartInfo.getCategoryName(), fitInPartInfo.getSubCategoryName()));
				this.fitInPartService.saveFitInParts(Arrays.asList(), parentFitInPartId);
			}
		}
	}

	@PostConstruct
	public void initialize() {
		log.debug("Initializing the DB with brands.");
		fitInPartService.initializeFitInPartTable();

		log.debug("Getting the list of pending fitinparts to scrap...");
		List<FitInPart> toScrap = this.fitInPartService.getPendingFitInParts();
		while (!toScrap.isEmpty()) {

			for (FitInPart fitinpart : toScrap) {
				FitInPartTag tag = fitinpart.getTag();
				Parameters parameters = new Gson().fromJson(fitinpart.getParameters(), Parameters.class);
				FitInPartInfo fitInPartInfo = new Gson().fromJson(fitinpart.getInfo(), FitInPartInfo.class);

				if (tag.equals(FitInPartTag.BRAND)) {
					taskExecutor.execute(() -> getModels(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.MODEL)) {
					taskExecutor.execute(() -> getYears(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.YEAR)) {
					taskExecutor.execute(() -> getBodies(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.BODY)) {
					taskExecutor.execute(() -> getEngines(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.ENGINE)) {
					taskExecutor.execute(() -> getEngineTypes(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.ENGINE_TYPE)) {
					taskExecutor.execute(() -> getCategories(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.CATEGORY)) {
					taskExecutor.execute(() -> getSubCategories(parameters, fitInPartInfo, fitinpart.getId()));
				} else if (tag.equals(FitInPartTag.SUB_CATEGORY)) {
					taskExecutor.execute(() -> getProductLinks(parameters, fitInPartInfo, fitinpart.getId()));
				}
			}

			try {
				log.debug("Sleeping...");
				Thread.sleep(10000l);
			} catch (InterruptedException interruptedException) {
				log.error("Error sleeping...", interruptedException);
			}
			toScrap = this.fitInPartService.getPendingFitInParts();
		}

		log.debug("Scrapping completed... ");
	}

	public static void main(String[] args) {
		SpringApplication.run(FitinpartScrapperApplication.class, args);
	}
}
