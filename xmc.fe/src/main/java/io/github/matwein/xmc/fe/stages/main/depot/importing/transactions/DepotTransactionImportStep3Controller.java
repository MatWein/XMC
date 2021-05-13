package io.github.matwein.xmc.fe.stages.main.depot.importing.transactions;

import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

@FxmlController
public class DepotTransactionImportStep3Controller {
	@FXML private TableView<DtoColumnMapping<DepotTransactionImportColmn>> columnMappingTable;
	@FXML private ValidationNumberField startWithLineNumberField;
	
	public TableView<DtoColumnMapping<DepotTransactionImportColmn>> getColumnMappingTable() {
		return columnMappingTable;
	}
	
	public ValidationNumberField getStartWithLineNumberField() {
		return startWithLineNumberField;
	}
}
