package io.github.matwein.xmc.fe.stages.main.cashaccount;

import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IDialogWithAsyncData;
import io.github.matwein.xmc.fe.ui.converter.CurrencyConverter;
import io.github.matwein.xmc.fe.ui.converter.DtoBankConverter;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationColorPicker;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationComboBox;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Currency;
import java.util.List;

@FxmlController
public class CashAccountEditController implements IDialogWithAsyncData<List<DtoBank>> {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;
	
	private Long cashAccountId;

    @FXML private ValidationComboBox<DtoBank> bankComboBox;
    @FXML private ValidationTextField nameTextfield;
    @FXML private ValidationTextField ibanTextfield;
    @FXML private ValidationTextField numberTextfield;
    @FXML private ValidationAutoComplete<Currency> currencyAutoComplete;
	@FXML private ValidationColorPicker colorPicker;

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
	
	    currencyAutoComplete.setContextMenuConverter(currencyConverter);
	    currencyAutoComplete.setConverter(Currency::getCurrencyCode);
    }

    @Override
    public void acceptAsyncData(List<DtoBank> data) {
        bankComboBox.getItems().addAll(data);
    }

    public ValidationComboBox<DtoBank> getBankComboBox() {
        return bankComboBox;
    }
	
	public ValidationTextField getNameTextfield() {
		return nameTextfield;
	}
	
	public ValidationTextField getIbanTextfield() {
		return ibanTextfield;
	}
	
	public ValidationTextField getNumberTextfield() {
		return numberTextfield;
	}
	
	public ValidationAutoComplete<Currency> getCurrencyAutoComplete() {
		return currencyAutoComplete;
	}
	
	public Long getCashAccountId() {
        return cashAccountId;
    }

    public void setCashAccountId(Long cashAccountId) {
        this.cashAccountId = cashAccountId;
    }
	
	public ValidationColorPicker getColorPicker() {
		return colorPicker;
	}
}
