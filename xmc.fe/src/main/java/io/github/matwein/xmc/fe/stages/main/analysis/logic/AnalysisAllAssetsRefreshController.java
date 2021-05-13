package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.services.analysis.IAnalysisAssetService;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.analysis.AnalysisContentController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnalysisAllAssetsRefreshController {
	private final AsyncProcessor asyncProcessor;
	private final IAnalysisAssetService analysisAssetService;
	private final AnalysisControllerFinder analysisControllerFinder;
	
	@Autowired
	public AnalysisAllAssetsRefreshController(
			AsyncProcessor asyncProcessor,
			IAnalysisAssetService analysisAssetService,
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
