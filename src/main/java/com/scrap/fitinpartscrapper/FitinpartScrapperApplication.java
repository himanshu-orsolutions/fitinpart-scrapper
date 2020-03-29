package com.scrap.fitinpartscrapper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.scrap.fitinpartscrapper.services.FitInPartService;

@SpringBootApplication
public class FitinpartScrapperApplication {

	@Autowired
	private FitInPartService fitInPartService;

	@PostConstruct
	public void initialize() {
		fitInPartService.initializeFitInPartTable();
	}

	public static void main(String[] args) {
		SpringApplication.run(FitinpartScrapperApplication.class, args);
	}
}
