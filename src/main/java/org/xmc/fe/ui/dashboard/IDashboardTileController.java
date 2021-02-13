package org.xmc.fe.ui.dashboard;

import org.xmc.fe.async.AsyncMonitor;

public interface IDashboardTileController {
	void loadAndApplyData(AsyncMonitor monitor, DtoDashboardTile tile);
}
