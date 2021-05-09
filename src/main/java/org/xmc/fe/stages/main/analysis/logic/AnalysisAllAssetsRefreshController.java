package org.xmc.fe.stages.main.analysis.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.AnalysisAssetService;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.stages.main.analysis.AnalysisContentController;

import java.util.Set;

@Component
public class AnalysisAllAssetsRefreshController {
	private final AsyncProcessor asyncProcessor;
	private final AnalysisAssetService analysisAssetService;
	private final AnalysisControllerFinder analysisControllerFinder;
	
	@Autowired
	public AnalysisAllAssetsRefreshController(
			AsyncProcessor asyncProcessor,
			AnalysisAssetService analysisAssetService,
			AnalysisControllerFinder analysisControllerFinder) {
		
		this.asyncProcessor = asyncProcessor;
		this.analysisAssetService = analysisAssetService;
		this.analysisControllerFinder = analysisControllerFinder;
	}
	
	public void refreshAllAssets() {
		Set<AnalysisContentController> analysisContentControllers = analysisControllerFinder.findAllAnalysisController();
		
		asyncProcessor.runAsync(
				analysisAssetService::loadAssets,
				result -> {
					for (AnalysisContentController controller : analysisContentControllers) {
						controller.updateAssetTree(result);
					}
				}
		);
	}
}
