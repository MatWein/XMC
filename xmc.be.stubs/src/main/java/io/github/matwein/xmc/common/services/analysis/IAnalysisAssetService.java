package io.github.matwein.xmc.common.services.analysis;

import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetSelection;

public interface IAnalysisAssetService {
	DtoAssetSelection loadAssets(IAsyncMonitor monitor);
}
