package org.xmc.fe.stages.main.administration.ccf;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.xmc.common.stubs.ccf.CurrencyConversionFactorOverviewFields;
import org.xmc.common.stubs.ccf.DtoCurrencyConversionFactor;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.components.table.ExtendedTable;

@FxmlController
public class CurrencyConversionFactorsController {
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	@FXML private ExtendedTable<DtoCurrencyConversionFactor, CurrencyConversionFactorOverviewFields> tableView;
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = tableView.getSelectionModel().selectedItemProperty().isNull();
		editButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		
//		tableView.setDataProvider(stockCategoryService::loadOverview);
		tableView.setDoubleClickConsumer(dtoCategoryOverview -> onEditCurrencyConversionFactor());
	}
	
	@FXML
	public void onNewCurrencyConversionFactor() {
	}
	
	@FXML
	public void onEditCurrencyConversionFactor() {
	}
	
	@FXML
	public void onDeleteCurrencyConversionFactor() {
	}
}
