package io.github.matwein.xmc.fe.stages.main.settings.content;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.importing.ImportTemplateService;
import io.github.matwein.xmc.common.stubs.importing.DtoImportTemplateOverview;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.AnalysisAllFavouritesRefreshController;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.BaseTable;

import java.util.List;

@FxmlController
public class SettingsImportTemplatesController {
	private final AsyncProcessor asyncProcessor;
	private final ImportTemplateService importTemplateService;
	private final AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController;
	
	@FXML private Button renameButton;
	@FXML private Button deleteButton;
	@FXML private BaseTable<DtoImportTemplateOverview> importTemplatesTable;
	@FXML private VBox importTemplatesRoot;
	
	@Autowired
	public SettingsImportTemplatesController(
			AsyncProcessor asyncProcessor,
			ImportTemplateService importTemplateService,
			AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController) {
		
		this.asyncProcessor = asyncProcessor;
		this.importTemplateService = importTemplateService;
		this.analysisAllFavouritesRefreshController = analysisAllFavouritesRefreshController;
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
				() -> {
					importTemplatesTable.getItems().remove(selectedItem);
					analysisAllFavouritesRefreshController.refreshAllFavourites();
				}
		);
	}
	
	@FXML
	public void onRenameCommit(CellEditEvent<Object, String> event) {
		String oldValue = event.getOldValue();
		String newValue = event.getNewValue();
		
		boolean valueChanged = !oldValue.equals(newValue);
		if (valueChanged) {
			long selectedTemplateId = importTemplatesTable.getSelectionModel().getSelectedItem().getId();
			
			asyncProcessor.runAsync(
					monitor -> importTemplateService.rename(monitor, selectedTemplateId, newValue),
					renmingSuccessful -> {
						if (renmingSuccessful) {
							analysisAllFavouritesRefreshController.refreshAllFavourites();
						} else {
							importTemplatesTable.edit(event.getTablePosition().getRow(), (TableColumn)event.getTableColumn());
						}
					}
			);
		}
	}
}
