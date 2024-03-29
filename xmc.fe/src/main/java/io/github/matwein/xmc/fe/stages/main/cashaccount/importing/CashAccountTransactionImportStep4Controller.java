package io.github.matwein.xmc.fe.stages.main.cashaccount.importing;

import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionImportService;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.CashAccountTransactionImportColmn;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
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
public class CashAccountTransactionImportStep4Controller implements IAfterInit<DtoImportData<CashAccountTransactionImportColmn>>, IValidationController {
    private final AsyncProcessor asyncProcessor;
    private final ICashAccountTransactionImportService cashAccountTransactionImportService;

    @FXML private CheckBox saveTemplateCheckbox;
    @FXML private ValidationTextField templateToSaveName;
    @FXML private BaseTable<DtoCashAccountTransaction> previewTable;
    @FXML private Text validTransactionCountText;
    @FXML private Text invalidTransactionCountText;
    @FXML private BaseTable<DtoImportFileValidationResultError> errorTable;
    @FXML private VBox rootVBox;

    private final SimpleBooleanProperty loading = new SimpleBooleanProperty(true);
    private final SimpleBooleanProperty fileErrors = new SimpleBooleanProperty(false);

    @Autowired
    public CashAccountTransactionImportStep4Controller(
            AsyncProcessor asyncProcessor,
            ICashAccountTransactionImportService cashAccountTransactionImportService) {

        this.asyncProcessor = asyncProcessor;
        this.cashAccountTransactionImportService = cashAccountTransactionImportService;
    }

    @FXML
    public void initialize() {
        templateToSaveName.disableProperty().bind(saveTemplateCheckbox.selectedProperty().not());
        rootVBox.disableProperty().bind(loading);
    }

    @Override
    public void afterInitialize(DtoImportData<CashAccountTransactionImportColmn> importData) {
        previewTable.getItems().clear();
        errorTable.getItems().clear();

        asyncProcessor.runAsync(
                () -> setLoading(true),
                monitor -> cashAccountTransactionImportService.readAndValidateImportFile(monitor, importData),
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

    private void afterValidateInputFile(DtoImportFileValidationResult<DtoCashAccountTransaction> validationResult) {
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
