package org.xmc.fe.stages.main.cashaccount.mapper;

import javafx.scene.control.ButtonBar.ButtonData;
import nl.garvelink.iban.IBAN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.fe.stages.main.cashaccount.CashAccountEditController;
import org.xmc.fe.ui.IDialogMapper;

import java.util.Currency;

@Component
public class CashAccountEditDialogMapper implements IDialogMapper<CashAccountEditController, DtoCashAccount> {
    @Override
    public void accept(CashAccountEditController controller, DtoCashAccount dtoCashAccount) {
        if (dtoCashAccount == null) {
            return;
        }

        controller.getBankComboBox().getSelectionModel().select(dtoCashAccount.getBank());
        controller.getCashAccountCurrencyAutoComplete().setText(dtoCashAccount.getCurrency().getCurrencyCode());
        controller.getCashAccountIbanTextfield().setText(dtoCashAccount.getIban());
        controller.getCashAccountNameTextfield().setText(dtoCashAccount.getName());
        controller.getCashAccountNumberTextfield().setText(dtoCashAccount.getNumber());
        controller.setCashAccountId(dtoCashAccount.getId());
    }

    @Override
    public DtoCashAccount apply(ButtonData buttonData, CashAccountEditController controller) {
        if (buttonData != ButtonData.OK_DONE) {
            return null;
        }

        var dtoCashAccount = new DtoCashAccount();

        dtoCashAccount.setBank(controller.getBankComboBox().getSelectionModel().getSelectedItem());
        dtoCashAccount.setCurrency(Currency.getInstance(controller.getCashAccountCurrencyAutoComplete().getText()));
        dtoCashAccount.setId(controller.getCashAccountId());
        dtoCashAccount.setName(controller.getCashAccountNameTextfield().getText());
        dtoCashAccount.setNumber(controller.getCashAccountNumberTextfield().getText());

        String iban = controller.getCashAccountIbanTextfield().getText();
        if (StringUtils.isNotBlank(iban)) {
            dtoCashAccount.setIban(IBAN.parse(StringUtils.trim(iban)).toString());
        }

        return dtoCashAccount;
    }
}
