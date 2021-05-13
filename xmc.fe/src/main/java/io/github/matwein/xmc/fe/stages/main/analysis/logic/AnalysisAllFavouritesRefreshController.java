package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.services.analysis.IAnalysisFavouriteService;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.analysis.AnalysisContentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnalysisAllFavouritesRefreshController {
	private final AsyncProcessor asyncProcessor;
	private final IAnalysisFavouriteService analysisFavouriteService;
	private final AnalysisControllerFinder analysisControllerFinder;
	
	@Autowired
	public AnalysisAllFavouritesRefreshController(
			AsyncProcessor asyncProcessor,
			IAnalysisFavouriteService analysisFavouriteService,
			AnalysisControllerFinder analysisControllerFinder) {
		
		this.asyncProcessor = asyncProcessor;
		this.analysisFavouriteService = analysisFavouriteService;
		this.analysisControllerFinder = analysisControllerFinder;
	}
	
	public void refreshAllFavourites() {
		Set<AnalysisContentController> analysisContentControllers = analysisControllerFinder.findAllAnalysisController();
		
		asyncProcessor.runAsync(
				analysisFavouriteService::loadAnalyseFavourites,
				result -> {
					for (AnalysisContentController controller : analysisContentControllers) {
						controller.updateFavouriteMenuButton(result);
					}
				}
		);
	}
}
