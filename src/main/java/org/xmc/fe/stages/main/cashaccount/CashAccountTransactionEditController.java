package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import org.xmc.common.stubs.category.DtoCategory;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.*;

import java.util.List;

@FxmlController
public class CashAccountTransactionEditController implements IDialogWithAsyncData<List<DtoCategory>> {
    @FXML private ValidationNumberField valueNumberField;
    @FXML private ValidationDatePicker valutaDatePicker;
    @FXML private ValidationNumberField saldoBeforeNumberField;
    @FXML private ValidationNumberField saldoAfterNumberField;
    @FXML private ValidationTextArea usageTextArea;
    @FXML private ValidationTextArea descriptionTextArea;
    @FXML private ValidationComboBox<DtoCategory> categoryComboBox;
    @FXML private ValidationTextField referenceBankTextfield;
    @FXML private ValidationTextField referenceIbanTextfield;
    @FXML private ValidationTextField referenceTextfield;
    @FXML private ValidationTextField creditorIdentifierTextfield;
    @FXML private ValidationTextField mandateTextfield;

    @FXML
    public void initialize() {
        categoryComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoCategory::getName));
        categoryComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SELECT_CATEGORY));
    }

    @Override
    public void acceptAsyncData(List<DtoCategory> data) {
        categoryComboBox.getItems().addAll(data);
    }

    public ValidationNumberField getValueNumberField() {
        return valueNumberField;
    }

    public ValidationDatePicker getValutaDatePicker() {
        return valutaDatePicker;
    }

    public ValidationNumberField getSaldoBeforeNumberField() {
        return saldoBeforeNumberField;
    }

    public ValidationNumberField getSaldoAfterNumberField() {
        return saldoAfterNumberField;
    }

    public ValidationTextArea getUsageTextArea() {
        return usageTextArea;
    }

    public ValidationTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public ValidationComboBox getCategoryComboBox() {
        return categoryComboBox;
    }

    public ValidationTextField getReferenceBankTextfield() {
        return referenceBankTextfield;
    }

    public ValidationTextField getReferenceIbanTextfield() {
        return referenceIbanTextfield;
    }

    public ValidationTextField getReferenceTextfield() {
        return referenceTextfield;
    }

    public ValidationTextField getCreditorIdentifierTextfield() {
        return creditorIdentifierTextfield;
    }

    public ValidationTextField getMandateTextfield() {
        return mandateTextfield;
    }
}
