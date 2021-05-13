package io.github.matwein.xmc.common.services.analysis;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.DtoAnalysisFavourite;
import io.github.matwein.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;

import java.util.List;
import java.util.Optional;

public interface IAnalysisFavouriteService {
	void saveOrUpdateAnalysisFavourite(IAsyncMonitor monitor, DtoAnalysisFavourite analysisToSave);
	
	List<DtoAnalysisFavourite> loadAnalyseFavourites(IAsyncMonitor monitor);
	
	Optional<DtoAnalysisFavourite> loadAnalyseFavourite(IAsyncMonitor monitor, long analysisFavouriteId);
	
	List<DtoImportAnalyseFavouriteOverview> loadAnalyseFavouritesOverview(IAsyncMonitor monitor);
	
	void delete(IAsyncMonitor monitor, long analysisId);
	
	boolean rename(IAsyncMonitor monitor, long analysisId, String newName);
}
