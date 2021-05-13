package io.github.matwein.xmc.fe.ui.dashboard;

import io.github.matwein.xmc.fe.async.AsyncMonitor;

public interface IDashboardTileController {
	boolean loadAndApplyData(AsyncMonitor monitor, DtoDashboardTile tile, DashboardContentTile contentTile);
}
