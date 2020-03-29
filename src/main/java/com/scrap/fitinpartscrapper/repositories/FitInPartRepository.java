package com.scrap.fitinpartscrapper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scrap.fitinpartscrapper.models.FitInPart;

@Repository
public interface FitInPartRepository extends JpaRepository<FitInPart, Long> {

}
