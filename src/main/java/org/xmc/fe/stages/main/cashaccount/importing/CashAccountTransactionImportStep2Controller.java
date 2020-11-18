package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationFileChooserField;

@FxmlController
public class CashAccountTransactionImportStep2Controller {
    @FXML private ValidationFileChooserField fileChooserField;
    @FXML private ValidationComboBox<?> fileTemplateComboBox;
    @FXML private RadioButton addOnlyRadioButton;
    @FXML private RadioButton addAndUpdateExistingRadioButton;

    public ValidationFileChooserField getFileChooserField() {
        return fileChooserField;
    }

    public ValidationComboBox<?> getFileTemplateComboBox() {
        return fileTemplateComboBox;
    }

    public RadioButton getAddOnlyRadioButton() {
        return addOnlyRadioButton;
    }

    public RadioButton getAddAndUpdateExistingRadioButton() {
        return addAndUpdateExistingRadioButton;
    }
}
