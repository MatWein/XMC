package org.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItem;
import org.xmc.fe.stages.main.depot.DepotItemEditController;
import org.xmc.fe.ui.IDialogMapper;

import java.util.Currency;

@Component
public class DepotItemEditDialogMapper implements IDialogMapper<DepotItemEditController, DtoDepotItem> {
	@Override
	public void accept(DepotItemEditController controller, DtoDepotItem dtoDepotItem) {
		if (dtoDepotItem == null) {
			return;
		}
		
		controller.setDepotItemId(dtoDepotItem.getId());
		controller.getAmountNumberField().setValue(dtoDepotItem.getAmount());
		controller.getCourseNumberField().setValue(dtoDepotItem.getCourse());
		controller.getCurrencyAutoComplete().setText(dtoDepotItem.getCurrency().getCurrencyCode());
		controller.getIsinAutoComplete().setText(dtoDepotItem.getIsin());
		controller.getValueNumberField().setValue(dtoDepotItem.getValue());
	}
	
	@Override
	public DtoDepotItem apply(ButtonData buttonData, DepotItemEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dto = new DtoDepotItem();
		
		dto.setId(controller.getDepotItemId());
		dto.setAmount(controller.getAmountNumberField().getValueAsBigDecimal());
		dto.setCourse(controller.getCourseNumberField().getValueAsBigDecimal());
		dto.setIsin(controller.getIsinAutoComplete().getTextOrNull());
		dto.setValue(controller.getValueNumberField().getValueAsBigDecimal());
		
		String currency = controller.getCurrencyAutoComplete().getTextOrNull();
		if (currency != null) {
			dto.setCurrency(Currency.getInstance(currency));
		}
		
		return dto;
	}
}
