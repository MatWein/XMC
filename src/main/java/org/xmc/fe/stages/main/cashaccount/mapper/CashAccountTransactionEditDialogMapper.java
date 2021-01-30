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

        if (dto.getCategory() != null) {
            controller.getCategoryComboBox().getSelectionModel().select(dto.getCategory());
        }

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

        dto.setId(controller.getTransactionId());
        dto.setCategory(controller.getCategoryComboBox().getSelectionModel().getSelectedItem());
        dto.setCreditorIdentifier(controller.getCreditorIdentifierTextfield().getTextOrNull());
        dto.setDescription(controller.getDescriptionTextArea().getTextOrNull());
        dto.setMandate(controller.getMandateTextfield().getTextOrNull());
        dto.setReference(controller.getReferenceTextfield().getTextOrNull());
        dto.setReferenceBank(controller.getReferenceBankTextfield().getTextOrNull());
        dto.setReferenceIban(controller.getReferenceIbanTextfield().getTextOrNull());
        dto.setUsage(controller.getUsageTextArea().getTextOrNull());
        dto.setValue(controller.getValueNumberField().getValueAsBigDecimal());
        dto.setValutaDate(controller.getValutaDatePicker().getValue());

        return dto;
    }
}
