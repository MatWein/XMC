package io.github.matwein.xmc.fe.stages.main.cashaccount;

import io.github.matwein.xmc.common.services.cashaccount.ICashAccountTransactionService;
import io.github.matwein.xmc.common.stubs.category.DtoCategory;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.common.ImageUtilFrontend;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IDialogWithAsyncData;
import io.github.matwein.xmc.fe.ui.components.ComboBoxIconCellFactory;
import io.github.matwein.xmc.fe.ui.components.FocusLostListener;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@FxmlController
public class CashAccountTransactionEditController implements IDialogWithAsyncData<Pair<List<DtoCategory>, Long>> {
    private final ICashAccountTransactionService cashAccountTransactionService;
    private final AsyncProcessor asyncProcessor;

    private Long transactionId;
    private long cashAccountId;

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
    @FXML private Button categoryDetectButton;

    @Autowired
    public CashAccountTransactionEditController(
		    ICashAccountTransactionService cashAccountTransactionService,
            AsyncProcessor asyncProcessor) {

        this.cashAccountTransactionService = cashAccountTransactionService;
        this.asyncProcessor = asyncProcessor;
    }

    @FXML
    public void initialize() {
        categoryComboBox.setCellFactory(new ComboBoxIconCellFactory<>(
                item -> ImageUtilFrontend.readFromByteArray$(item.getIcon()),
                DtoCategory::getName));

        categoryComboBox.setConverter(GenericItemToStringConverter.getInstance(DtoCategory::getName));
        categoryComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_TRANSACTION_EDIT_SELECT_CATEGORY));

        valueNumberField.focusedProperty().addListener(FocusLostListener.getInstance(valueNumberField, this::updateSaldoPreview));
        valutaDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateSaldoPreview());
    }

    @Override
    public void acceptAsyncData(Pair<List<DtoCategory>, Long> data) {
        cashAccountId = data.getRight();

        categoryComboBox.getItems().add(null);
        categoryComboBox.getItems().addAll(data.getLeft());
        
        updateSaldoPreview();
    }

    @FXML
    public void autoDetectCategory() {
        asyncProcessor.runAsync(
                () -> {
                    categoryComboBox.setDisable(true);
                    categoryDetectButton.setDisable(true);
                },
                monitor -> cashAccountTransactionService.autoDetectCategory(monitor, cashAccountId, usageTextArea.getTextOrNull()),
                result -> result.ifPresent(foundCategoryId -> {
                    categoryComboBox.getItems().stream()
		                    .filter(Objects::nonNull)
                            .filter(dto -> foundCategoryId.equals(dto.getId()))
                            .findFirst()
                            .ifPresent(dtoCategory -> categoryComboBox.getSelectionModel().select(dtoCategory));
                }),
                () -> {
                    categoryComboBox.setDisable(false);
                    categoryDetectButton.setDisable(false);
                }
        );
    }

    private void updateSaldoPreview() {
        if (valueNumberField.isValid() && valutaDatePicker.isValid()) {
            Pair<BigDecimal, BigDecimal> saldoPreview = cashAccountTransactionService.calculateSaldoPreview(
                    cashAccountId,
                    transactionId,
                    valutaDatePicker.getValue(),
                    valueNumberField.getValueAsBigDecimal());

            saldoBeforeNumberField.setValue(saldoPreview.getLeft());
            saldoAfterNumberField.setValue(saldoPreview.getRight());
        }
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

    public ValidationComboBox<DtoCategory> getCategoryComboBox() {
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}
