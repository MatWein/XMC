package io.github.matwein.xmc.fe.stages.main.depot;

import javafx.fxml.FXML;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationDatePicker;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTimePicker;

@FxmlController
public class DepotDeliveryEditController {
	private Long deliveryId;
	
	@FXML private ValidationDatePicker deliveryDatePicker;
	@FXML private ValidationTimePicker deliveryTimePicker;
	
	public Long getDeliveryId() {
		return deliveryId;
	}
	
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	public ValidationDatePicker getDeliveryDatePicker() {
		return deliveryDatePicker;
	}
	
	public ValidationTimePicker getDeliveryTimePicker() {
		return deliveryTimePicker;
	}
}
