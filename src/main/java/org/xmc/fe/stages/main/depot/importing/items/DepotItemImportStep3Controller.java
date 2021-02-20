package org.xmc.fe.stages.main.depot.importing.items;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xmc.common.stubs.depot.items.DepotItemImportColmn;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

@FxmlController
public class DepotItemImportStep3Controller {
	@FXML private TableView<DtoColumnMapping<DepotItemImportColmn>> columnMappingTable;
	@FXML private ValidationNumberField startWithLineNumberField;
	
	public TableView<DtoColumnMapping<DepotItemImportColmn>> getColumnMappingTable() {
		return columnMappingTable;
	}
	
	public ValidationNumberField getStartWithLineNumberField() {
		return startWithLineNumberField;
	}
}
