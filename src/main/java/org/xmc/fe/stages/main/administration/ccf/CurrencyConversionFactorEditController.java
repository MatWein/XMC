package org.xmc.fe.stages.main.administration.ccf;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.converter.CurrencyConverter;
import org.xmc.fe.ui.validation.components.ValidationDatePicker;
import org.xmc.fe.ui.validation.components.ValidationNumberField;
import org.xmc.fe.ui.validation.components.ValidationTimePicker;
import org.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;

@FxmlController
public class CurrencyConversionFactorEditController {
	private final CurrencyConverter currencyConverter;
	
	private Long currencyConversionFactorId;
	
	@FXML private ValidationDatePicker inputDatePicker;
	@FXML private ValidationTimePicker inputTimePicker;
	@FXML private ValidationAutoComplete<Currency> sourceCurrencyAutoComplete;
	@FXML private ValidationNumberField conversionFactorNumberField;
	
	@Autowired
	public CurrencyConversionFactorEditController(CurrencyConverter currencyConverter) {
		this.currencyConverter = currencyConverter;
	}
	
	@FXML
	public void initialize() {
		sourceCurrencyAutoComplete.setContextMenuConverter(currencyConverter);
		sourceCurrencyAutoComplete.setConverter(Currency::getCurrencyCode);
	}
	
	public ValidationDatePicker getInputDatePicker() {
		return inputDatePicker;
	}
	
	public ValidationTimePicker getInputTimePicker() {
		return inputTimePicker;
	}
	
	public ValidationAutoComplete<Currency> getSourceCurrencyAutoComplete() {
		return sourceCurrencyAutoComplete;
	}
	
	public ValidationNumberField getConversionFactorNumberField() {
		return conversionFactorNumberField;
	}
	
	public Long getCurrencyConversionFactorId() {
		return currencyConversionFactorId;
	}
	
	public void setCurrencyConversionFactorId(Long currencyConversionFactorId) {
		this.currencyConversionFactorId = currencyConversionFactorId;
	}
}
