package org.xmc.fe.stages.main.depot.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.xmc.fe.stages.main.depot.DepotTransactionEditController;
import org.xmc.fe.ui.IDialogMapper;

import java.util.Currency;

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
		controller.getCurrencyAutoComplete().setText(dtoDepotTransaction.getCurrency().getCurrencyCode());
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
		dto.setValutaDate(controller.getValutaDatePicker().getValue());
		
		String currencyCode = controller.getCurrencyAutoComplete().getTextOrNull();
		if (currencyCode != null) {
			dto.setCurrency(Currency.getInstance(currencyCode));
		}
		
		return dto;
	}
}