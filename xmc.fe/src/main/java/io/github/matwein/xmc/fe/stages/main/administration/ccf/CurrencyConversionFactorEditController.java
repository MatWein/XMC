package io.github.matwein.xmc.fe.stages.main.administration.ccf;

import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.converter.CurrencyConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationDatePicker;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTimePicker;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

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
