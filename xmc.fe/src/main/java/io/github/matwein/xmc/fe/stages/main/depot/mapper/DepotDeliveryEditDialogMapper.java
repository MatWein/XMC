package io.github.matwein.xmc.fe.stages.main.depot.mapper;

import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import io.github.matwein.xmc.fe.stages.main.depot.DepotDeliveryEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

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
		dto.setDeliveryDate(controller.getDeliveryDatePicker().getValueOrNull().atTime(controller.getDeliveryTimePicker().getValue()));
		
		return dto;
	}
}
