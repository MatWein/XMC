package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationTextField;

@FxmlController
public class CashAccountTransactionImportStep4Controller {
    @FXML private CheckBox saveTemplateCheckbox;
    @FXML private ValidationTextField templateToSaveName;
    @FXML private TableView previewTable;

    @FXML
    public void initialize() {
        templateToSaveName.disableProperty().bind(saveTemplateCheckbox.selectedProperty().not());
    }

    public CheckBox getSaveTemplateCheckbox() {
        return saveTemplateCheckbox;
    }

    public ValidationTextField getTemplateToSaveName() {
        return templateToSaveName;
    }
}
