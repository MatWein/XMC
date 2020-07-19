package org.xmc.fe.stages.main.cashaccount;

import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IDialogWithAsyncData;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;
import org.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;
import java.util.List;

@FxmlController
public class CashAccountEditController implements IDialogWithAsyncData<List<DtoBank>> {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;

    private Long cashAccountId;

    @FXML private ValidationComboBox<DtoBank> bankComboBox;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationAutoComplete<Currency> cashAccountCurrencyAutoComplete;

    @Autowired
    public CashAccountEditController(
            CurrencyConverter currencyConverter,
            DtoBankConverter dtoBankConverter) {

        this.currencyConverter = currencyConverter;
        this.dtoBankConverter = dtoBankConverter;
    }

    @FXML
    public void initialize() {
        bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
        bankComboBox.setPromptText(MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_SELECT_BANK));

        cashAccountCurrencyAutoComplete.setContextMenuConverter(currencyConverter);
        cashAccountCurrencyAutoComplete.setConverter(Currency::getCurrencyCode);
    }

    @Override
    public void acceptAsyncData(List<DtoBank> data) {
        bankComboBox.getItems().addAll(data);
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
