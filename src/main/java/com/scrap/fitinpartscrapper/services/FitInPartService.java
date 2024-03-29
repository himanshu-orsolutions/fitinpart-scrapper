package com.scrap.fitinpartscrapper.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.FitInPart;
import com.scrap.fitinpartscrapper.models.FitInPartInfo;
import com.scrap.fitinpartscrapper.models.FitInPartTag;
import com.scrap.fitinpartscrapper.models.Option;
import com.scrap.fitinpartscrapper.models.Parameters;
import com.scrap.fitinpartscrapper.repositories.FitInPartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FitInPartService {

	@Autowired
	private KeyCollector keyCollector;

	@Autowired
	private FitInPartRepository fitInPartRepository;

	@Transactional
	public void initializeFitInPartTable() {

		log.debug("Checking is DB is already initialized or not...");
		if (fitInPartRepository.count() == 0) {
			log.debug("Initializing 'fitinpart' table with brands information...");
			List<Option> brands = keyCollector.getBrands();
			if (!brands.isEmpty()) {
				List<FitInPart> fitInParts = new ArrayList<>();
				brands.forEach(brand -> fitInParts.add(FitInPart.builder()
						.info(new Gson().toJson(FitInPartInfo.builder().brandName(brand.getName()).build()))
						.parameters(new Gson().toJson(Parameters.builder().brandId(brand.getId()).build()))
						.tag(FitInPartTag.BRAND).isScrapped(false).build()));

				fitInPartRepository.saveAll(fitInParts);
			} else {
				log.error("No brands found. Check the API calls before proceeding further.");
			}
		}
	}

	@Transactional
	public List<FitInPart> getPendingFitInParts() {

		return this.fitInPartRepository.getPendingFitInParts();
	}

	@Transactional
	public void saveFitInParts(List<FitInPart> fitInParts, long parentFitInPartId) {

		this.fitInPartRepository.saveAll(fitInParts);
		FitInPart parentFitInPart = this.fitInPartRepository.findById(parentFitInPartId).get();
		parentFitInPart.setIsScrapped(true);
		this.fitInPartRepository.save(parentFitInPart);
	}
}
