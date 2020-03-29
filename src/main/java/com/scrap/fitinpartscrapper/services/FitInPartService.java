package com.scrap.fitinpartscrapper.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scrap.fitinpartscrapper.collectors.KeyCollector;
import com.scrap.fitinpartscrapper.models.FitInPart;
import com.scrap.fitinpartscrapper.models.FitInPartTag;
import com.scrap.fitinpartscrapper.models.Option;
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

		if (fitInPartRepository.count() == 0) {
			try {
				List<Option> brands = keyCollector.getBrands();
				if (!brands.isEmpty()) {
					List<FitInPart> fitInParts = new ArrayList<>();
					brands.forEach(brand -> {
						fitInParts.add(FitInPart.builder().parentId(-1l).info(brand.getName()).tag(FitInPartTag.BRAND)
								.nextURL("https://www.fitinpart.sg/index.php?route=module/appsearch/getModel")
								.httpBody("class=0&brand=" + brand.getId() + "&veh_type=1").httpMethod("POST")
								.isScrapped(false).build());
					});

					fitInPartRepository.saveAll(fitInParts);
				} else {
					log.error("No brands found. Check the API calls before proceeding further.");
				}
			} catch (IOException ioException) {
				log.error("Error fetching the brands. It has to be fixed at P1.", ioException);
			}
		}
	}
}
