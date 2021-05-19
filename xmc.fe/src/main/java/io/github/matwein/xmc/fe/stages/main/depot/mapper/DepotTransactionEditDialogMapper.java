package io.github.matwein.xmc.fe.stages.main.depot.mapper;

import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.fe.stages.main.depot.DepotTransactionEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionEditDialogMapper implements IDialogMapper<DepotTransactionEditController, DtoDepotTransaction> {
	@Override
	public void accept(DepotTransactionEditController controller, DtoDepotTransaction dtoDepotTransaction) {
		if (dtoDepotTransaction == null) {
			return;
		}
		
		controller.setTransactionId(dtoDepotTransaction.getId());
		controller.getAmountNumberField().setValue(dtoDepotTransaction.getAmount());
		controller.getCourseNumberField().setValue(dtoDepotTransaction.getCourse());
		controller.getCurrencyAutoComplete().setText(dtoDepotTransaction.getCurrency());
		controller.getDescriptionTextArea().setText(dtoDepotTransaction.getDescription());
		controller.getIsinAutoComplete().setText(dtoDepotTransaction.getIsin());
		controller.getValueNumberField().setValue(dtoDepotTransaction.getValue());
		controller.getValutaDatePicker().setValue(dtoDepotTransaction.getValutaDate());
	}
	
	@Override
	public DtoDepotTransaction apply(ButtonData buttonData, DepotTransactionEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dto = new DtoDepotTransaction();
		
		dto.setAmount(controller.getAmountNumberField().getValueAsBigDecimal());
		dto.setCourse(controller.getCourseNumberField().getValueAsBigDecimal());
		dto.setDescription(controller.getDescriptionTextArea().getTextOrNull());
		dto.setId(controller.getTransactionId());
		dto.setIsin(controller.getIsinAutoComplete().getTextOrNull());
		dto.setValue(controller.getValueNumberField().getValueAsBigDecimal());
		dto.setValutaDate(controller.getValutaDatePicker().getValueOrNull());
		
		String currencyCode = controller.getCurrencyAutoComplete().getTextOrNull();
		if (currencyCode != null) {
			dto.setCurrency(currencyCode);
		}
		
		return dto;
	}
}
