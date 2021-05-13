package io.github.matwein.xmc.be.services.analysis;

import io.github.matwein.xmc.be.services.analysis.controller.AssetSelectionLoadingController;
import io.github.matwein.xmc.common.services.analysis.IAnalysisAssetService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnalysisAssetService implements IAnalysisAssetService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisAssetService.class);
	
	private final AssetSelectionLoadingController assetSelectionLoadingController;
	
	@Autowired
	public AnalysisAssetService(AssetSelectionLoadingController assetSelectionLoadingController) {
		this.assetSelectionLoadingController = assetSelectionLoadingController;
	}
	
	@Override
	public DtoAssetSelection loadAssets(IAsyncMonitor monitor) {
		LOGGER.info("Loading all selectable assets for analysis.");
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_LOAD_SELECTABLE_ASSETS_FOR_ANALYSIS));
		
		return assetSelectionLoadingController.loadSelectableAssets();
	}
}
