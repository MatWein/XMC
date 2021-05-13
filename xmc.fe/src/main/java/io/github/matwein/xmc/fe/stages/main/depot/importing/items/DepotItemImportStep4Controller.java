package io.github.matwein.xmc.fe.stages.main.depot.importing.items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.matwein.xmc.common.services.depot.IDepotItemImportService;
import io.github.matwein.xmc.common.stubs.depot.items.DepotItemImportColmn;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;
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

@FxmlController
public class DepotItemImportStep4Controller implements IAfterInit<DtoImportData<DepotItemImportColmn>>, IValidationController {
	private final AsyncProcessor asyncProcessor;
	private final IDepotItemImportService depotItemImportService;
	
	@FXML private CheckBox saveTemplateCheckbox;
	@FXML private ValidationTextField templateToSaveName;
	@FXML private BaseTable<DtoDepotItemImportRow> previewTable;
	@FXML private Text validTransactionCountText;
	@FXML private Text invalidTransactionCountText;
	@FXML private BaseTable<DtoImportFileValidationResultError> errorTable;
	@FXML private VBox rootVBox;
	
	private final SimpleBooleanProperty loading = new SimpleBooleanProperty(true);
	private final SimpleBooleanProperty fileErrors = new SimpleBooleanProperty(false);
	
	@Autowired
	public DepotItemImportStep4Controller(
			AsyncProcessor asyncProcessor,
			IDepotItemImportService depotItemImportService) {
		
		this.asyncProcessor = asyncProcessor;
		this.depotItemImportService = depotItemImportService;
	}
	
	@FXML
	public void initialize() {
		templateToSaveName.disableProperty().bind(saveTemplateCheckbox.selectedProperty().not());
		rootVBox.disableProperty().bind(loading);
	}
	
	@Override
	public void afterInitialize(DtoImportData<DepotItemImportColmn> importData) {
		previewTable.getItems().clear();
		errorTable.getItems().clear();
		
		asyncProcessor.runAsync(
				() -> setLoading(true),
				monitor -> depotItemImportService.readAndValidateImportFile(monitor, importData),
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
	
	private void afterValidateInputFile(DtoImportFileValidationResult<DtoDepotItemImportRow> validationResult) {
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
