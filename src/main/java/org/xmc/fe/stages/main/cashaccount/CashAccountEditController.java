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
import org.xmc.common.stubs.DtoBank;
import org.xmc.common.stubs.DtoBankInformation;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankInformationBicConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankInformationBlzConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.ImageSelectionButton;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.IValidationController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;
import org.xmc.fe.ui.validation.components.autocomplete.ValidationAutoComplete;

import java.util.Currency;

@Component
public class CashAccountEditController implements IValidationController {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;
    private final BankService bankService;
    private final DtoBankInformationBlzConverter dtoBankInformationBlzConverter;
    private final DtoBankInformationBicConverter dtoBankInformationBicConverter;

    @FXML private ImageSelectionButton logoButton;
    @FXML private TitledPane bankTitledPane;
    @FXML private ValidationComboBox<DtoBank> bankComboBox;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankNameAutoComplete;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBicAutoComplete;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBlzAutoComplete;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationAutoComplete<Currency> cashAccountCurrencyAutoComplete;

    @Autowired
    public CashAccountEditController(
            CurrencyConverter currencyConverter,
            DtoBankConverter dtoBankConverter,
            BankService bankService,
            DtoBankInformationBlzConverter dtoBankInformationBlzConverter,
            DtoBankInformationBicConverter dtoBankInformationBicConverter) {

        this.currencyConverter = currencyConverter;
        this.dtoBankConverter = dtoBankConverter;
        this.bankService = bankService;
        this.dtoBankInformationBlzConverter = dtoBankInformationBlzConverter;
        this.dtoBankInformationBicConverter = dtoBankInformationBicConverter;
    }

    @FXML
    public void initialize() {
        DtoBank emptyBank = new DtoBank();

        bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
        bankComboBox.getItems().add(emptyBank);
        bankComboBox.getItems().addAll(bankService.loadAllBanks());
        bankComboBox.getSelectionModel().selectedItemProperty().addListener(createBankChangeListener());
        bankComboBox.getSelectionModel().select(emptyBank);

        bankNameAutoComplete.setContextMenuConverter(dtoBankInformationBlzConverter);
        bankNameAutoComplete.setConverter(DtoBankInformation::getBankName);
        bankNameAutoComplete.setItemSelectedConsumer(item -> {
            bankBlzAutoComplete.selectItem(item);
            bankBicAutoComplete.selectItem(item);
        });

        bankBlzAutoComplete.setContextMenuConverter(dtoBankInformationBlzConverter);
        bankBlzAutoComplete.setConverter(DtoBankInformation::getBlz);
        bankBlzAutoComplete.setItemSelectedConsumer(item -> {
            bankNameAutoComplete.selectItem(item);
            bankBicAutoComplete.selectItem(item);
        });

        bankBicAutoComplete.setContextMenuConverter(dtoBankInformationBicConverter);
        bankBicAutoComplete.setConverter(DtoBankInformation::getBic);
        bankBicAutoComplete.setItemSelectedConsumer(item -> {
            bankNameAutoComplete.selectItem(item);
            bankBlzAutoComplete.selectItem(item);
        });

        cashAccountCurrencyAutoComplete.setContextMenuConverter(currencyConverter);
        cashAccountCurrencyAutoComplete.setConverter(Currency::getCurrencyCode);
    }

    private ChangeListener<DtoBank> createBankChangeListener() {
        return (observable, oldValue, newValue) -> {
            boolean existingBankSelected = newValue.getId() != null;
            bankTitledPane.setDisable(existingBankSelected);

            if (existingBankSelected) {
                bankNameAutoComplete.setText(newValue.getName());
                bankBicAutoComplete.setText(newValue.getBic());
                bankBlzAutoComplete.setText(newValue.getBlz());
                logoButton.setImage(newValue.getLogo());
            } else {
                bankNameAutoComplete.clear();
                bankBicAutoComplete.clear();
                bankBlzAutoComplete.clear();
                logoButton.setImage((Image)null);
            }
        };
    }

    @Override
    public Multimap<IValidationComponent, String> validate() {
        Multimap<IValidationComponent, String> validationErrors = ArrayListMultimap.create();

        if (bankComboBox.getSelectionModel().getSelectedItem().getId() == null && StringUtils.isBlank(bankNameAutoComplete.getText())) {
            validationErrors.put(bankNameAutoComplete, MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }

        return validationErrors;
    }
}
