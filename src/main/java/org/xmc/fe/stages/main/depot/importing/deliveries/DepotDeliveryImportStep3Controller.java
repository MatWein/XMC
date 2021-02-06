package org.xmc.fe.stages.main.depot.importing.deliveries;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.xmc.common.stubs.depot.deliveries.DepotDeliveryImportColmn;
import org.xmc.common.stubs.importing.DtoColumnMapping;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationNumberField;

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
