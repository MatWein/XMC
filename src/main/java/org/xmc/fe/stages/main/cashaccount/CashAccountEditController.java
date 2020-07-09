package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.bank.BankService;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;
import org.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;

@FxmlController
public class CashAccountEditController {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;
    private final BankService bankService;

    private Long cashAccountId;

    @FXML private ValidationComboBox<DtoBank> bankComboBox;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationAutoComplete<Currency> cashAccountCurrencyAutoComplete;

    @Autowired
    public CashAccountEditController(
            CurrencyConverter currencyConverter,
            DtoBankConverter dtoBankConverter,
            BankService bankService) {

        this.currencyConverter = currencyConverter;
        this.dtoBankConverter = dtoBankConverter;
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
        bankComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_ADD_BANK));
        bankComboBox.getItems().addAll(bankService.loadAllBanks());

        cashAccountCurrencyAutoComplete.setContextMenuConverter(currencyConverter);
        cashAccountCurrencyAutoComplete.setConverter(Currency::getCurrencyCode);
    }

    public ValidationComboBox<DtoBank> getBankComboBox() {
        return bankComboBox;
    }

    public ValidationTextField getCashAccountNameTextfield() {
        return cashAccountNameTextfield;
    }

    public ValidationTextField getCashAccountIbanTextfield() {
        return cashAccountIbanTextfield;
    }

    public ValidationTextField getCashAccountNumberTextfield() {
        return cashAccountNumberTextfield;
    }

    public ValidationAutoComplete<Currency> getCashAccountCurrencyAutoComplete() {
        return cashAccountCurrencyAutoComplete;
    }

    public Long getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(Long cashAccountId) {
        this.cashAccountId = cashAccountId;
    }
}
