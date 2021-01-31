package org.xmc.fe.stages.main.depot;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.common.stubs.stocks.DtoMinimalStock;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.converter.CurrencyConverter;
import org.xmc.fe.ui.converter.StockConverter;
import org.xmc.fe.ui.validation.components.ValidationNumberField;
import org.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;

@FxmlController
public class DepotItemEditController {
	private final CurrencyConverter currencyConverter;
	private final StockConverter stockConverter;
	
	@FXML private ValidationNumberField amountNumberField;
	@FXML private ValidationNumberField courseNumberField;
	@FXML private ValidationNumberField valueNumberField;
	@FXML private ValidationAutoComplete<DtoMinimalStock> isinAutoComplete;
	@FXML private ValidationAutoComplete<Currency> currencyAutoComplete;
	
	private Long depotItemId;
	
	@Autowired
	public DepotItemEditController(
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
	
	public Long getDepotItemId() {
		return depotItemId;
	}
	
	public void setDepotItemId(Long depotItemId) {
		this.depotItemId = depotItemId;
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
	
	public ValidationAutoComplete<DtoMinimalStock> getIsinAutoComplete() {
		return isinAutoComplete;
	}
	
	public ValidationAutoComplete<Currency> getCurrencyAutoComplete() {
		return currencyAutoComplete;
	}
}
