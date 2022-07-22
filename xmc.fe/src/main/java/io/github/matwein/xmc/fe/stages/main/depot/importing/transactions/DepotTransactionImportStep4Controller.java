package io.github.matwein.xmc.fe.stages.main.depot.importing.transactions;

import io.github.matwein.xmc.common.services.depot.IDepotTransactionImportService;
import io.github.matwein.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import io.github.matwein.xmc.common.stubs.importing.DtoImportData;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResult;
import io.github.matwein.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterInit;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.components.table.BaseTable;
import io.github.matwein.xmc.fe.ui.validation.IValidationComponent;
import io.github.matwein.xmc.fe.ui.validation.IValidationController;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@FxmlController
public class DepotTransactionImportStep4Controller implements IAfterInit<DtoImportData<DepotTransactionImportColmn>>, IValidationController {
	private final AsyncProcessor asyncProcessor;
	private final IDepotTransactionImportService depotTransactionImportService;
	
	@FXML private CheckBox saveTemplateCheckbox;
	@FXML private ValidationTextField templateToSaveName;
	@FXML private BaseTable<DtoDepotTransactionImportRow> previewTable;
	@FXML private Text validTransactionCountText;
	@FXML private Text invalidTransactionCountText;
	@FXML private BaseTable<DtoImportFileValidationResultError> errorTable;
	@FXML private VBox rootVBox;
	
	private final SimpleBooleanProperty loading = new SimpleBooleanProperty(true);
	private final SimpleBooleanProperty fileErrors = new SimpleBooleanProperty(false);
	
	@Autowired
	public DepotTransactionImportStep4Controller(
			AsyncProcessor asyncProcessor,
			IDepotTransactionImportService depotTransactionImportService) {
		
		this.asyncProcessor = asyncProcessor;
		this.depotTransactionImportService = depotTransactionImportService;
	}
	
	@FXML
	public void initialize() {
		templateToSaveName.disableProperty().bind(saveTemplateCheckbox.selectedProperty().not());
		rootVBox.disableProperty().bind(loading);
	}
	
	@Override
	public void afterInitialize(DtoImportData<DepotTransactionImportColmn> importData) {
		previewTable.getItems().clear();
		errorTable.getItems().clear();
		
		asyncProcessor.runAsync(
				() -> setLoading(true),
				monitor -> depotTransactionImportService.readAndValidateImportFile(monitor, importData),
				this::afterValidateInputFile,
				() -> setLoading(false)
		);
	}
	
	private void setLoading(boolean loading) {
		this.loading.set(loading);
		
		if (templateToSaveName.getScene() != null) {
			SceneUtil.getOrCreateValidationSceneState(templateToSaveName.getScene()).validate();
		}
	}
	
	private void afterValidateInputFile(DtoImportFileValidationResult<DtoDepotTransactionImportRow> validationResult) {
		fileErrors.set(CollectionUtils.isNotEmpty(validationResult.getErrors()));
		
		validTransactionCountText.setText(String.valueOf(validationResult.getValidTransactionCount()));
		invalidTransactionCountText.setText(String.valueOf(validationResult.getInvalidTransactionCount()));
		
		errorTable.getItems().clear();
		errorTable.getItems().addAll(validationResult.getErrors());
		
		previewTable.getItems().clear();
		previewTable.getItems().addAll(validationResult.getSuccessfullyReadLines());
	}
	
	public CheckBox getSaveTemplateCheckbox() {
		return saveTemplateCheckbox;
	}
	
	public ValidationTextField getTemplateToSaveName() {
		return templateToSaveName;
	}
	
	@Override
	public Map<IValidationComponent, List<String>> validate() {
		return defaultValidate(loading, fileErrors);
	}
}
