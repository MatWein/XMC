package org.xmc.fe.stages.main.cashaccount.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.xmc.fe.stages.main.cashaccount.CashAccountTransactionEditController;
import org.xmc.fe.ui.IDialogMapper;

@Component
public class CashAccountTransactionEditDialogMapper implements IDialogMapper<CashAccountTransactionEditController, DtoCashAccountTransaction> {
    @Override
    public void accept(CashAccountTransactionEditController controller, DtoCashAccountTransaction dto) {
        if (dto == null) {
            return;
        }

        controller.setTransactionId(dto.getId());
        controller.getCategoryComboBox().getSelectionModel().select(dto.getCategory());
        controller.getCreditorIdentifierTextfield().setText(dto.getCreditorIdentifier());
        controller.getDescriptionTextArea().setText(dto.getDescription());
        controller.getMandateTextfield().setText(dto.getMandate());
        controller.getReferenceTextfield().setText(dto.getReference());
        controller.getReferenceBankTextfield().setText(dto.getReferenceBank());
        controller.getReferenceIbanTextfield().setText(dto.getReferenceIban());
        controller.getUsageTextArea().setText(dto.getUsage());
        controller.getValueNumberField().setValue(dto.getValue());
        controller.getValutaDatePicker().setValue(dto.getValutaDate());
    }

    @Override
    public DtoCashAccountTransaction apply(ButtonData buttonData, CashAccountTransactionEditController controller) {
        if (buttonData != ButtonData.OK_DONE) {
            return null;
        }

        var dto = new DtoCashAccountTransaction();

        dto.setCategory(controller.getCategoryComboBox().getSelectionModel().getSelectedItem());
        dto.setCreditorIdentifier(controller.getCreditorIdentifierTextfield().getText());
        dto.setDescription(controller.getDescriptionTextArea().getText());
        dto.setId(controller.getTransactionId());
        dto.setMandate(controller.getMandateTextfield().getText());
        dto.setReference(controller.getReferenceTextfield().getText());
        dto.setReferenceBank(controller.getReferenceBankTextfield().getText());
        dto.setReferenceIban(controller.getReferenceIbanTextfield().getText());
        dto.setUsage(controller.getUsageTextArea().getText());
        dto.setValue(controller.getValueNumberField().getValueAsBigDecimal());
        dto.setValutaDate(controller.getValutaDatePicker().getValue());

        return dto;
    }
}
