package org.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.fe.stages.main.depot.DepotTransactionEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class DepotTransactionEditDialogMapper implements IDialogMapper<DepotTransactionEditController, DtoDepotTransaction> {
	@Override
	public void accept(DepotTransactionEditController controller, DtoDepotTransaction dtoDepotTransaction) {
		if (dtoDepotTransaction == null) {
			return;
		}
		
		
	}
	
	@Override
	public DtoDepotTransaction apply(ButtonData buttonData, DepotTransactionEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		return null;
	}
}
