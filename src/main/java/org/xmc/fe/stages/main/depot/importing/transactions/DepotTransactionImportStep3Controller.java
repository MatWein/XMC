package org.xmc.fe.stages.main.depot.importing.transactions;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

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
