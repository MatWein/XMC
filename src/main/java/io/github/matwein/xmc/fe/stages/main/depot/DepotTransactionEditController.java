package io.github.matwein.xmc.fe.stages.main.depot;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.common.stubs.stocks.DtoMinimalStock;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.converter.CurrencyConverter;
import io.github.matwein.xmc.fe.ui.converter.StockConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationDatePicker;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextArea;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;

@FxmlController
public class DepotTransactionEditController {
	private final CurrencyConverter currencyConverter;
	private final StockConverter stockConverter;
	
	private Long transactionId;
	
	@FXML private ValidationNumberField amountNumberField;
	@FXML private ValidationNumberField courseNumberField;
	@FXML private ValidationNumberField valueNumberField;
	@FXML private ValidationDatePicker valutaDatePicker;
	@FXML private ValidationAutoComplete<DtoMinimalStock> isinAutoComplete;
	@FXML private ValidationTextArea descriptionTextArea;
	@FXML private ValidationAutoComplete<Currency> currencyAutoComplete;
	
	@Autowired
	public DepotTransactionEditController(
			CurrencyConverter currencyConverter,
			StockConverter stockConverter) {
		
		this.currencyConverter = currencyConverter;
		this.stockConverter = stockConverter;
	}
	
	@FXML
	public void initialize() {
		currencyAutoComplete.setContextMenuConverter(currencyConverter);
		currencyAutoComplete.setConverter(Currency::getCurrencyCode);
		
		isinAutoComplete.setContextMenuConverter(stockConverter);
		isinAutoComplete.setConverter(DtoMinimalStock::getIsin);
	}
	
	public ValidationNumberField getAmountNumberField() {
		return amountNumberField;
	}
	
	public ValidationNumberField getCourseNumberField() {
		return courseNumberField;
	}
	
	public ValidationNumberField getValueNumberField() {
		return valueNumberField;
	}
	
	public ValidationDatePicker getValutaDatePicker() {
		return valutaDatePicker;
	}
	
	public ValidationAutoComplete<DtoMinimalStock> getIsinAutoComplete() {
		return isinAutoComplete;
	}
	
	public ValidationTextArea getDescriptionTextArea() {
		return descriptionTextArea;
	}
	
	public ValidationAutoComplete<Currency> getCurrencyAutoComplete() {
		return currencyAutoComplete;
	}
	
	public Long getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
}
