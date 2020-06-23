package org.xmc.fe.stages.main.cashaccount;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.bank.BankService;
import org.xmc.be.services.currency.CurrencyService;
import org.xmc.common.stubs.DtoBank;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.ImageSelectionButton;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.IValidationController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

import java.util.Currency;

@Component
public class CashAccountEditController implements IValidationController {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;
    private final CurrencyService currencyService;
    private final BankService bankService;

    @FXML private ImageSelectionButton logoButton;
    @FXML private TitledPane bankTitledPane;
    @FXML private ValidationComboBox<DtoBank> bankComboBox;
    @FXML private ValidationTextField bankNameTextfield;
    @FXML private ValidationTextField bankBicTextfield;
    @FXML private ValidationTextField bankBlzTextfield;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationComboBox<Currency> cashAccountCurrencyComboBox;

    @Autowired
    public CashAccountEditController(
            CurrencyConverter currencyConverter,
            DtoBankConverter dtoBankConverter,
            CurrencyService currencyService,
            BankService bankService) {

        this.currencyConverter = currencyConverter;
        this.dtoBankConverter = dtoBankConverter;
        this.currencyService = currencyService;
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        DtoBank emptyBank = new DtoBank();

        bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
        bankComboBox.getItems().add(emptyBank);
        bankComboBox.getItems().addAll(bankService.loadAllBanks());
        bankComboBox.getSelectionModel().selectedItemProperty().addListener(createBankChangeListener());
        bankComboBox.getSelectionModel().select(emptyBank);

        cashAccountCurrencyComboBox.setConverter(GenericItemToStringConverter.getInstance(currencyConverter));
        cashAccountCurrencyComboBox.getItems().addAll(currencyService.loadAllCurrencies());
    }

    private ChangeListener<DtoBank> createBankChangeListener() {
        return (observable, oldValue, newValue) -> {
            boolean existingBankSelected = newValue.getId() != null;
            bankTitledPane.setDisable(existingBankSelected);

            if (existingBankSelected) {
                bankNameTextfield.setText(newValue.getName());
                bankBicTextfield.setText(newValue.getBic());
                bankBlzTextfield.setText(newValue.getBlz());
                logoButton.setImage(newValue.getLogo());
            } else {
                bankNameTextfield.setText(null);
                bankBicTextfield.setText(null);
                bankBlzTextfield.setText(null);
                logoButton.setImage((Image)null);
            }
        };
    }

    @Override
    public Multimap<IValidationComponent, String> validate() {
        Multimap<IValidationComponent, String> validationErrors = ArrayListMultimap.create();

        if (bankComboBox.getSelectionModel().getSelectedItem().getId() == null && StringUtils.isBlank(bankNameTextfield.getText())) {
            validationErrors.put(bankNameTextfield, MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }

        return validationErrors;
    }
}
