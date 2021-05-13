package io.github.matwein.xmc.be.services.analysis;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.analysis.AnalysisFavourite;
import io.github.matwein.xmc.be.repositories.analysis.AnalysisFavouriteJpaRepository;
import io.github.matwein.xmc.be.services.analysis.controller.AnalysisFavouriteRenameController;
import io.github.matwein.xmc.be.services.analysis.controller.AnalysisFavouriteSavingController;
import io.github.matwein.xmc.be.services.analysis.mapper.AnalyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper;
import io.github.matwein.xmc.be.services.analysis.mapper.AnalysisFavouriteToDtoAnalysisFavouriteMapper;
import io.github.matwein.xmc.common.services.analysis.IAnalysisFavouriteService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnalysisFavouriteService implements IAnalysisFavouriteService {
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
	
	@Override
	public void saveOrUpdateAnalysisFavourite(IAsyncMonitor monitor, DtoAnalysisFavourite analysisToSave) {
		LOGGER.info("Saving analysis favourite: {}.", analysisToSave);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_SAVE_ANALYSIS_FAVOURITE));
		
		analysisFavouriteSavingController.saveOrUpdate(analysisToSave);
	}
	
	@Override
	public List<DtoAnalysisFavourite> loadAnalyseFavourites(IAsyncMonitor monitor) {
		LOGGER.info("Loading analysis favourites.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES));
		
		List<AnalysisFavourite> analysisFavourites = analysisFavouriteJpaRepository.findAll();
		return analysisFavouriteToDtoAnalysisFavouriteMapper.mapAll(analysisFavourites);
	}
	
	@Override
	public Optional<DtoAnalysisFavourite> loadAnalyseFavourite(IAsyncMonitor monitor, long analysisFavouriteId) {
		LOGGER.info("Loading analysis favourite with id '{}'.", analysisFavouriteId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES));
		
		return analysisFavouriteJpaRepository.findById(analysisFavouriteId)
				.map(analysisFavouriteToDtoAnalysisFavouriteMapper::map);
	}
	
	@Override
	public List<DtoImportAnalyseFavouriteOverview> loadAnalyseFavouritesOverview(IAsyncMonitor monitor) {
		LOGGER.info("Loading analysis favourites overview.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_ANALYSIS_FAVOURITES));
		
		List<AnalysisFavourite> analysisFavourites = analysisFavouriteJpaRepository.findAll();
		return analyseFavouriteToDtoImportAnalyseFavouriteOverviewMapper.mapAll(analysisFavourites);
	}
	
	@Override
	public void delete(IAsyncMonitor monitor, long analysisId) {
		LOGGER.info("Deleting analysis favourite with id '{}'.", analysisId);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_DELETE_ANALYSIS_FAVOURITE));
		
		analysisFavouriteJpaRepository.deleteById(analysisId);
	}
	
	@Override
	public boolean rename(IAsyncMonitor monitor, long analysisId, String newName) {
		LOGGER.info("Renaming analysis favourite with id '{}' to '{}'..", analysisId, newName);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_RENAME_ANALYSIS_FAVOURITE));
		
		return analysisFavouriteRenameController.rename(analysisId, newName);
	}
}
