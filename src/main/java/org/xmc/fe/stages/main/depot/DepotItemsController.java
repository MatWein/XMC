package org.xmc.fe.stages.main.depot;

import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;

@FxmlController
public class DepotItemsController implements IAfterInit<DepotController> {
	private DepotController parentController;
	
	@Override
	public void afterInitialize(DepotController parentController) {
		this.parentController = parentController;
	}
}
