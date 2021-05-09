package io.github.matwein.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;
import io.github.matwein.xmc.fe.stages.main.depot.DepotEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;

@Component
public class DepotEditDialogMapper implements IDialogMapper<DepotEditController, DtoDepot> {
	@Override
	public void accept(DepotEditController controller, DtoDepot dtoDepot) {
		if (dtoDepot == null) {
			return;
		}
		
		controller.getBankComboBox().getSelectionModel().select(dtoDepot.getBank());
		controller.getDepotNameTextfield().setText(dtoDepot.getName());
		controller.getDepotNumberTextfield().setText(dtoDepot.getNumber());
		controller.setDepotId(dtoDepot.getId());
		controller.getColorPicker().setValueHex(dtoDepot.getColor());
	}
	
	@Override
	public DtoDepot apply(ButtonData buttonData, DepotEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dtoDepot = new DtoDepot();
		
		dtoDepot.setBank(controller.getBankComboBox().getSelectionModel().getSelectedItem());
		dtoDepot.setId(controller.getDepotId());
		dtoDepot.setName(controller.getDepotNameTextfield().getTextOrNull());
		dtoDepot.setNumber(controller.getDepotNumberTextfield().getTextOrNull());
		dtoDepot.setColor(controller.getColorPicker().getValueHex());
		
		return dtoDepot;
	}
}
