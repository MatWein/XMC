package org.xmc.fe.ui.dashboard;

import org.xmc.fe.async.AsyncMonitor;

public interface IDashboardTileController {
	boolean loadAndApplyData(AsyncMonitor monitor, DtoDashboardTile tile, DashboardContentTile contentTile);
}
