package org.xmc.fe.stages.main.settings.content;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.importing.ImportTemplateService;
import org.xmc.common.stubs.importing.DtoImportTemplateOverview;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.components.table.BaseTable;
import org.xmc.fe.ui.validation.components.ValidationTextField;

import java.util.List;

@FxmlController
public class SettingsImportTemplatesController {
	private final AsyncProcessor asyncProcessor;
	private final ImportTemplateService importTemplateService;
	
	@FXML private Button renameButton;
	@FXML private Button deleteButton;
	@FXML private BaseTable<DtoImportTemplateOverview> importTemplatesTable;
	@FXML private VBox importTemplatesRoot;
	
	@Autowired
	public SettingsImportTemplatesController(
			AsyncProcessor asyncProcessor,
			ImportTemplateService importTemplateService) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = importTemplatesTable.getSelectionModel().selectedItemProperty().isNull();
		renameButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		asyncProcessor.runAsync(
				() -> importTemplatesRoot.setDisable(true),
				this::loadSettings,
				this::applySettings,
				() -> importTemplatesRoot.setDisable(false)
		);
	}
	
	private List<DtoImportTemplateOverview> loadSettings(AsyncMonitor monitor) {
		return importTemplateService.loadImportTemplatesOverview(monitor);
	}
	
	private void applySettings(List<DtoImportTemplateOverview> templates) {
		importTemplatesTable.getItems().clear();
		importTemplatesTable.getItems().addAll(templates);
	}
	
	@FXML
	public void onRename() {
		TablePosition selectedCell = importTemplatesTable.getSelectionModel().getSelectedCells().get(0);
		int row = selectedCell.getRow();
		TableColumn column = selectedCell.getTableColumn();
		
		importTemplatesTable.edit(row, column);
	}
	
	@FXML
	public void onDelete() {
		DtoImportTemplateOverview selectedItem = importTemplatesTable.getSelectionModel().getSelectedItem();
		long selectedTemplateId = selectedItem.getId();
		
		asyncProcessor.runAsyncVoid(
				() -> {},
				monitor -> importTemplateService.delete(monitor, selectedTemplateId),
				() -> importTemplatesTable.getItems().remove(selectedItem)
		);
	}
	
	@FXML
	public void onRenameCommit(TableColumn.CellEditEvent<Object, String> event) {
		String oldValue = event.getOldValue();
		String newValue = event.getNewValue();
		
		boolean valueChanged = !oldValue.equals(newValue);
		if (valueChanged && newValue.length() > 0 && newValue.length() < ValidationTextField.MAX_LENGTH) {
			long selectedTemplateId = importTemplatesTable.getSelectionModel().getSelectedItem().getId();
			
			asyncProcessor.runAsyncVoid(
					monitor -> importTemplateService.rename(monitor, selectedTemplateId, newValue)
			);
		}
	}
}
