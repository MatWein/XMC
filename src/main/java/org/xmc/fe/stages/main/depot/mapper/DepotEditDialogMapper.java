package org.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.DtoDepot;
import org.xmc.fe.stages.main.depot.DepotEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class DepotEditDialogMapper implements IDialogMapper<DepotEditController, DtoDepot> {
	@Override
	public void accept(DepotEditController controller, DtoDepot dtoDepot) {
		if (dtoDepot == null) {
			return;
		}
		
		
	}
	
	@Override
	public DtoDepot apply(ButtonData buttonData, DepotEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		return null;
	}
}
