package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.CommonConstants;
import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.be.repositories.analysis.AnalysisFavouriteJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnalysisFavouriteRenameController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisFavouriteRenameController.class);
	
	private final AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository;
	
	@Autowired
	public AnalysisFavouriteRenameController(AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository) {
		this.analysisFavouriteJpaRepository = analysisFavouriteJpaRepository;
	}
	
	public boolean rename(long analysisId, String newName) {
		if (StringUtils.isBlank(newName)) {
			return false;
		}
		if (newName.length() >= CommonConstants.MAX_TEXT_LENGTH) {
			return false;
		}
		
		Optional<AnalysisFavourite> existingAnalysis = analysisFavouriteJpaRepository.findByName(newName);
		if (existingAnalysis.isPresent()) {
			LOGGER.warn("Could not rename analysis with id '{}' to '{}' because name is already in use.", analysisId, newName);
			return false;
		}
		
		AnalysisFavourite analysisFavourite = analysisFavouriteJpaRepository.getOne(analysisId);
		analysisFavourite.setName(newName);
		analysisFavouriteJpaRepository.save(analysisFavourite);
		
		return true;
	}
}
