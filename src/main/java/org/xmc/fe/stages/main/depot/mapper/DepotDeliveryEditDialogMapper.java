package org.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.xmc.fe.stages.main.depot.DepotDeliveryEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class DepotDeliveryEditDialogMapper implements IDialogMapper<DepotDeliveryEditController, DtoDepotDelivery> {
	@Override
	public void accept(DepotDeliveryEditController controller, DtoDepotDelivery dtoDepotDelivery) {
		if (dtoDepotDelivery == null) {
			return;
		}
		
		controller.setDeliveryId(dtoDepotDelivery.getId());
		controller.getDeliveryDatePicker().setValue(dtoDepotDelivery.getDeliveryDate().toLocalDate());
		controller.getDeliveryTimePicker().setValue(dtoDepotDelivery.getDeliveryDate().toLocalTime());
	}
	
	@Override
	public DtoDepotDelivery apply(ButtonData buttonData, DepotDeliveryEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dto = new DtoDepotDelivery();
		
		dto.setId(controller.getDeliveryId());
		dto.setDeliveryDate(controller.getDeliveryDatePicker().getValue().atTime(controller.getDeliveryTimePicker().getValue()));
		
		return dto;
	}
}
