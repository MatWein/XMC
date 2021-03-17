package org.xmc.fe.stages.main.depot.importing.transactions;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.depot.DepotTransactionImportService;
import org.xmc.common.stubs.depot.transactions.DepotTransactionImportColmn;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import org.xmc.common.stubs.importing.DtoImportData;
import org.xmc.common.stubs.importing.DtoImportFileValidationResult;
import org.xmc.common.stubs.importing.DtoImportFileValidationResultError;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterInit;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.components.table.BaseTable;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.IValidationController;
import org.xmc.fe.ui.validation.components.ValidationTextField;

@FxmlController
public class DepotTransactionImportStep4Controller implements IAfterInit<DtoImportData<DepotTransactionImportColmn>>, IValidationController {
	private final AsyncProcessor asyncProcessor;
	private final DepotTransactionImportService depotTransactionImportService;
	
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
			DepotTransactionImportService depotTransactionImportService) {
		
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
	public Multimap<IValidationComponent, String> validate() {
		Multimap<IValidationComponent, String> errors = ArrayListMultimap.create();
		
		if (loading.get() || fileErrors.get()) {
			errors.put(null, "Input file not valid or already loading.");
		}
		
		return errors;
	}
}
