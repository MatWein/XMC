package org.xmc.fe.stages.main.cashaccount.importing;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import org.xmc.common.stubs.importing.CsvSeparator;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationFileChooserField;

@FxmlController
public class CashAccountTransactionImportStep2Controller {
	@FXML private ValidationFileChooserField fileChooserField;
    @FXML private ValidationComboBox<?> fileTemplateComboBox;
    @FXML private RadioButton addAllRadioButton;
    @FXML private RadioButton addOnlyRadioButton;
    @FXML private RadioButton addAndUpdateExistingRadioButton;
	@FXML private ValidationComboBox<CsvSeparator> csvSeparatorComboBox;
	
	@FXML
	public void initialize() {
		csvSeparatorComboBox.setConverter(GenericItemToStringConverter.getInstance(t -> MessageAdapter.getByKey(MessageKey.IMPORTING_CSV_SEPARATOR, t)));
	}

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
	
	public ValidationComboBox<CsvSeparator> getCsvSeparatorComboBox() {
		return csvSeparatorComboBox;
	}
	
	public RadioButton getAddAllRadioButton() {
		return addAllRadioButton;
	}
}
