package com.scrap.fitinpartscrapper.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scrap.fitinpartscrapper.models.FitInPart;

@Repository
public interface FitInPartRepository extends JpaRepository<FitInPart, Long> {

	@Query(nativeQuery = true, value = "select * from fitinpart where is_scrapped=false and tag <> 'PRODUCT' limit 50")
	List<FitInPart> getPendingFitInParts();
}
