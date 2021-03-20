package org.xmc.be.services.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.services.analysis.controller.AssetSelectionLoadingController;
import org.xmc.common.stubs.analysis.DtoAssetSelection;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

@Service
@Transactional
public class AnalysisAssetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisAssetService.class);
	
	private final AssetSelectionLoadingController assetSelectionLoadingController;
	
	@Autowired
	public AnalysisAssetService(AssetSelectionLoadingController assetSelectionLoadingController) {
		this.assetSelectionLoadingController = assetSelectionLoadingController;
	}
	
	public DtoAssetSelection loadAssets(AsyncMonitor monitor) {
		LOGGER.info("Loading all selectable assets for analysis.");
		monitor.setStatusText(MessageKey.ASYNC_TASK_LOAD_SELECTABLE_ASSETS_FOR_ANALYSIS);
		
		return assetSelectionLoadingController.loadSelectableAssets();
	}
}
