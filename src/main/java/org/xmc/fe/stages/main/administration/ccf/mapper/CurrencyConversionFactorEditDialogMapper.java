package org.xmc.fe.stages.main.administration.ccf.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.xmc.fe.stages.main.administration.ccf.CurrencyConversionFactorEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class CurrencyConversionFactorEditDialogMapper implements IDialogMapper<CurrencyConversionFactorEditController, DtoCurrencyConversionFactor> {
	@Override
	public void accept(CurrencyConversionFactorEditController controller, DtoCurrencyConversionFactor dto) {
		if (dto == null) {
			return;
		}
		
		
	}
	
	@Override
	public DtoCurrencyConversionFactor apply(ButtonData buttonData, CurrencyConversionFactorEditController currencyConversionFactorEditController) {
		if (buttonData != ButtonData.OK_DONE) {
			return null;
		}
		
		return null;
	}
}
