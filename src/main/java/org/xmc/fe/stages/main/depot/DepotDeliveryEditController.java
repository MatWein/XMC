package org.xmc.fe.stages.main.depot;

import javafx.fxml.FXML;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;
import org.xmc.fe.ui.validation.components.ValidationTimePicker;

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
