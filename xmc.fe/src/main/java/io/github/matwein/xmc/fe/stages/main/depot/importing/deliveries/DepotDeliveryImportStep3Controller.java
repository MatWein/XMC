package io.github.matwein.xmc.fe.stages.main.depot.importing.deliveries;

import io.github.matwein.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import io.github.matwein.xmc.common.stubs.importing.DtoColumnMapping;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationNumberField;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

@FxmlController
public class DepotDeliveryImportStep3Controller {
	@FXML private TableView<DtoColumnMapping<DepotDeliveryImportColmn>> columnMappingTable;
	@FXML private ValidationNumberField startWithLineNumberField;
	
	public TableView<DtoColumnMapping<DepotDeliveryImportColmn>> getColumnMappingTable() {
		return columnMappingTable;
	}
	
	public ValidationNumberField getStartWithLineNumberField() {
		return startWithLineNumberField;
	}
}
