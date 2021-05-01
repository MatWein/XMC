package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.AnalysisFavouriteService;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.stages.main.analysis.AnalysisContentController;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AnalysisAllFavouritesRefreshController {
	private final AsyncProcessor asyncProcessor;
	private final AnalysisFavouriteService analysisFavouriteService;
	
	@Autowired
	public AnalysisAllFavouritesRefreshController(
			AsyncProcessor asyncProcessor,
			AnalysisFavouriteService analysisFavouriteService) {
		
		this.asyncProcessor = asyncProcessor;
		this.analysisFavouriteService = analysisFavouriteService;
	}
	
	public void refreshAllFavourites() {
		Set<AnalysisContentController> analysisContentControllers = findAllAnalysisController();
		
		asyncProcessor.runAsync(
				analysisFavouriteService::loadAnalyseFavourites,
				result -> {
					for (AnalysisContentController controller : analysisContentControllers) {
						controller.updateFavouriteMenuButton(result);
					}
				}
		);
	}
	
	private Set<AnalysisContentController> findAllAnalysisController() {
		Set<Node> buttons = MainController.mainWindow.getScene().getRoot().lookupAll("#favouriteMenuButton");
		
		return buttons.stream()
				.map(button -> (AnalysisContentController)button.getUserData())
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}
}
