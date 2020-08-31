package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationFileChooserField;

@FxmlController
public class CashAccountTransactionImportStep2Controller {
    @FXML private ValidationFileChooserField fileChooserField;
    @FXML private ValidationComboBox<?> fileTemplateComboBox;

    @FXML
    public void initialize() {

    }

    public ValidationFileChooserField getFileChooserField() {
        return fileChooserField;
    }
}
