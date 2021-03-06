package io.github.matwein.xmc.fe.stages.main.administration.ccf.mapper;

import io.github.matwein.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import io.github.matwein.xmc.fe.stages.main.administration.ccf.CurrencyConversionFactorEditController;
import io.github.matwein.xmc.fe.ui.IDialogMapper;
import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class CurrencyConversionFactorEditDialogMapper implements IDialogMapper<CurrencyConversionFactorEditController, DtoCurrencyConversionFactor> {
	@Override
	public void accept(CurrencyConversionFactorEditController controller, DtoCurrencyConversionFactor dto) {
		if (dto == null) {
			return;
		}
		
		controller.getConversionFactorNumberField().setValue(dto.getFactorToEur());
		controller.getInputDatePicker().setValue(dto.getInputDate().toLocalDate());
		controller.getInputTimePicker().setValue(dto.getInputDate().toLocalTime());
		controller.getSourceCurrencyAutoComplete().selectItem(Currency.getInstance(dto.getCurrency()));
		controller.setCurrencyConversionFactorId(dto.getId());
	}
	
	@Override
	public DtoCurrencyConversionFactor apply(ButtonData buttonData, CurrencyConversionFactorEditController controller) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		var dto = new DtoCurrencyConversionFactor();
		
		dto.setCurrency(controller.getSourceCurrencyAutoComplete().getTextOrNull());
		dto.setFactorToEur(controller.getConversionFactorNumberField().getValueAsBigDecimal());
		dto.setId(controller.getCurrencyConversionFactorId());
		dto.setInputDate(controller.getInputDatePicker().getValueOrNull().atTime(controller.getInputTimePicker().getValue()));
		
		return dto;
	}
}
