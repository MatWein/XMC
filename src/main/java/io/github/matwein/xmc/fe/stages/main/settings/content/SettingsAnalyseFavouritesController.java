package io.github.matwein.xmc.fe.stages.main.settings.content;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.services.analysis.AnalysisFavouriteService;
import io.github.matwein.xmc.common.stubs.analysis.DtoImportAnalyseFavouriteOverview;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.analysis.logic.AnalysisAllFavouritesRefreshController;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.components.table.BaseTable;

import java.util.List;

@FxmlController
public class SettingsAnalyseFavouritesController {
	private final AsyncProcessor asyncProcessor;
	private final AnalysisFavouriteService analysisFavouriteService;
	private final AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController;
	
	@FXML private Button renameButton;
	@FXML private Button deleteButton;
	@FXML private BaseTable<DtoImportAnalyseFavouriteOverview> analyseFavouritesTable;
	@FXML private VBox analyseFavouritesRoot;
	
	@Autowired
	public SettingsAnalyseFavouritesController(
			AsyncProcessor asyncProcessor,
			AnalysisFavouriteService analysisFavouriteService,
			AnalysisAllFavouritesRefreshController analysisAllFavouritesRefreshController) {
		
		this.asyncProcessor = asyncProcessor;
		this.analysisFavouriteService = analysisFavouriteService;
		this.analysisAllFavouritesRefreshController = analysisAllFavouritesRefreshController;
	}
	
	@FXML
	public void initialize() {
		BooleanBinding noTableItemSelected = analyseFavouritesTable.getSelectionModel().selectedItemProperty().isNull();
		renameButton.disableProperty().bind(noTableItemSelected);
		deleteButton.disableProperty().bind(noTableItemSelected);
		
		asyncProcessor.runAsync(
				() -> analyseFavouritesRoot.setDisable(true),
				this::loadSettings,
				this::applySettings,
				() -> analyseFavouritesRoot.setDisable(false)
		);
	}
	
	private List<DtoImportAnalyseFavouriteOverview> loadSettings(AsyncMonitor monitor) {
		return analysisFavouriteService.loadAnalyseFavouritesOverview(monitor);
	}
	
	private void applySettings(List<DtoImportAnalyseFavouriteOverview> items) {
		analyseFavouritesTable.getItems().clear();
		analyseFavouritesTable.getItems().addAll(items);
	}
	
	@FXML
	public void onRename() {
		TablePosition selectedCell = analyseFavouritesTable.getSelectionModel().getSelectedCells().get(0);
		int row = selectedCell.getRow();
		TableColumn column = selectedCell.getTableColumn();
		
		analyseFavouritesTable.edit(row, column);
	}
	
	@FXML
	public void onDelete() {
		DtoImportAnalyseFavouriteOverview selectedItem = analyseFavouritesTable.getSelectionModel().getSelectedItem();
		long selectedAnalysisId = selectedItem.getId();
		
		asyncProcessor.runAsyncVoid(
				() -> {},
				monitor -> analysisFavouriteService.delete(monitor, selectedAnalysisId),
				() -> {
					analyseFavouritesTable.getItems().remove(selectedItem);
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
			long selectedAnalysisId = analyseFavouritesTable.getSelectionModel().getSelectedItem().getId();
			
			asyncProcessor.runAsync(
					monitor -> analysisFavouriteService.rename(monitor, selectedAnalysisId, newValue),
					renmingSuccessful -> {
						if (renmingSuccessful) {
							analysisAllFavouritesRefreshController.refreshAllFavourites();
						} else {
							analyseFavouritesTable.edit(event.getTablePosition().getRow(), (TableColumn)event.getTableColumn());
						}
					}
			);
		}
	}
}
