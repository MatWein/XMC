package io.github.matwein.xmc.fe.stages.main.depot.importing.items;

import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

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
