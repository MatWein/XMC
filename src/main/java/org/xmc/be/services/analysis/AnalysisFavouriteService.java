package org.xmc.be.services.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.analysis.AnalysisFavourite;
import org.xmc.be.repositories.analysis.AnalysisFavouriteJpaRepository;
import org.xmc.be.services.analysis.controller.AnalysisFavouriteSavingController;
import org.xmc.be.services.analysis.mapper.AnalysisFavouriteToDtoAnalysisFavouriteMapper;
import org.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;

@Service
@Transactional
public class AnalysisFavouriteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisFavouriteService.class);
	
	private final AnalysisFavouriteSavingController analysisFavouriteSavingController;
	private final AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository;
	private final AnalysisFavouriteToDtoAnalysisFavouriteMapper analysisFavouriteToDtoAnalysisFavouriteMapper;
	
	@Autowired
	public AnalysisFavouriteService(
			AnalysisFavouriteSavingController analysisFavouriteSavingController,
			AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository,
			AnalysisFavouriteToDtoAnalysisFavouriteMapper analysisFavouriteToDtoAnalysisFavouriteMapper) {
		
		this.analysisFavouriteSavingController = analysisFavouriteSavingController;
		this.analysisFavouriteJpaRepository = analysisFavouriteJpaRepository;
		this.analysisFavouriteToDtoAnalysisFavouriteMapper = analysisFavouriteToDtoAnalysisFavouriteMapper;
	}
	
	public void saveOrUpdateAnalysisFavourite(AsyncMonitor monitor, DtoAnalysisFavourite analysisToSave) {
		LOGGER.info("Saving analysis favourite: {}.", analysisToSave);
		monitor.setStatusText(MessageKey.ASYNC_TASK_SAVE_ANALYSIS_FAVOURITE);
		
		analysisFavouriteSavingController.saveOrUpdate(analysisToSave);
	}
	
	public List<DtoAnalysisFavourite> loadAnalyseFavourites(AsyncMonitor monitor) {
		LOGGER.info("Loading analysis favourites.");
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES);
		
		List<AnalysisFavourite> analysisFavourites = analysisFavouriteJpaRepository.findAll();
		return analysisFavouriteToDtoAnalysisFavouriteMapper.mapAll(analysisFavourites);
	}
}
