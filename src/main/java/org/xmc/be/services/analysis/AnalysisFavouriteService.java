package org.xmc.be.services.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.entities.analysis.AnalysisFavourite;
import org.xmc.be.repositories.analysis.AnalysisFavouriteJpaRepository;
import org.xmc.be.services.analysis.controller.AnalysisFavouriteRenameController;
import org.xmc.be.services.analysis.controller.AnalysisFavouriteSavingController;
import org.xmc.be.services.analysis.mapper.AnalyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper;
import org.xmc.be.services.analysis.mapper.AnalysisFavouriteToDtoAnalysisFavouriteMapper;
import org.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import org.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnalysisFavouriteService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisFavouriteService.class);
	
	private final AnalysisFavouriteSavingController analysisFavouriteSavingController;
	private final AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository;
	private final AnalysisFavouriteToDtoAnalysisFavouriteMapper analysisFavouriteToDtoAnalysisFavouriteMapper;
	private final AnalyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper;
	private final AnalysisFavouriteRenameController analysisFavouriteRenameController;
	
	@Autowired
	public AnalysisFavouriteService(
			AnalysisFavouriteSavingController analysisFavouriteSavingController,
			AnalysisFavouriteJpaRepository analysisFavouriteJpaRepository,
			AnalysisFavouriteToDtoAnalysisFavouriteMapper analysisFavouriteToDtoAnalysisFavouriteMapper,
			AnalyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper,
			AnalysisFavouriteRenameController analysisFavouriteRenameController) {
		
		this.analysisFavouriteSavingController = analysisFavouriteSavingController;
		this.analysisFavouriteJpaRepository = analysisFavouriteJpaRepository;
		this.analysisFavouriteToDtoAnalysisFavouriteMapper = analysisFavouriteToDtoAnalysisFavouriteMapper;
		this.analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper = analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper;
		this.analysisFavouriteRenameController = analysisFavouriteRenameController;
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
	
	public Optional<DtoAnalysisFavourite> loadAnalyseFavourite(AsyncMonitor monitor, long analysisFavouriteId) {
		LOGGER.info("Loading analysis favourite with id '{}'.", analysisFavouriteId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES);
		
		return analysisFavouriteJpaRepository.findById(analysisFavouriteId)
				.map(analysisFavouriteToDtoAnalysisFavouriteMapper::map);
	}
	
	public List<DtoImportAnalyseFavouriteOverview> loadAnalyseFavouritesOverview(AsyncMonitor monitor) {
		LOGGER.info("Loading analysis favourites overview.");
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES);
		
		List<AnalysisFavourite> analysisFavourites = analysisFavouriteJpaRepository.findAll();
		return analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper.mapAll(analysisFavourites);
	}
	
	public void delete(AsyncMonitor monitor, long analysisId) {
		LOGGER.info("Deleting analysis favourite with id '{}'.", analysisId);
		monitor.setStatusText(MessageKey.ASYNC_TASK_DELETE_ANALYSIS_FAVOURITE);
		
		analysisFavouriteJpaRepository.deleteById(analysisId);
	}
	
	public boolean rename(AsyncMonitor monitor, long analysisId, String newName) {
		LOGGER.info("Renaming analysis favourite with id '{}' to '{}'..", analysisId, newName);
		monitor.setStatusText(MessageKey.ASYNC_TASK_RENAME_ANALYSIS_FAVOURITE);
		
		return analysisFavouriteRenameController.rename(analysisId, newName);
	}
}
