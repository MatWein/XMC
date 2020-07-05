package org.xmc.fe.stages.main.cashaccount.mapper;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import nl.garvelink.iban.IBAN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBank;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.xmc.fe.stages.main.cashaccount.CashAccountEditController;

import java.util.Currency;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Component
public class CashAccountEditDialogMapper implements BiFunction<ButtonType, CashAccountEditController, DtoCashAccount>, BiConsumer<CashAccountEditController, DtoCashAccount> {
    @Override
    public DtoCashAccount apply(ButtonType buttonType, CashAccountEditController controller) {
        if (buttonType.getButtonData() != ButtonBar.ButtonData.OK_DONE) {
            return null;
        }

        DtoBank selectedBank = controller.getBankComboBox().getSelectionModel().getSelectedItem();

        var dtoCashAccount = new DtoCashAccount();

        if (selectedBank == null) {
            selectedBank = new DtoBank();
            selectedBank.setName(controller.getBankNameAutoComplete().getText());
            selectedBank.setLogo(controller.getLogoButton().getImageAsByteArray());
            selectedBank.setId(null);
            selectedBank.setBlz(controller.getBankBlzAutoComplete().getText());
            selectedBank.setBic(controller.getBankBicAutoComplete().getText());
        }

        dtoCashAccount.setBank(selectedBank);
        dtoCashAccount.setCurrency(Currency.getInstance(controller.getCashAccountCurrencyAutoComplete().getText()));
        dtoCashAccount.setIban(IBAN.parse(StringUtils.trim(controller.getCashAccountIbanTextfield().getText())).toString());
        dtoCashAccount.setId(controller.getCashAccountId());
        dtoCashAccount.setName(controller.getCashAccountNameTextfield().getText());
        dtoCashAccount.setNumber(controller.getCashAccountNumberTextfield().getText());

        return dtoCashAccount;
    }

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
}
