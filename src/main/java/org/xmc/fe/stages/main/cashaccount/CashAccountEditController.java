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
import org.xmc.fe.ui.validation.components.ValidationAutoComplete;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

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
    @FXML private ValidationTextField bankNameTextfield;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBicComboBox;
    @FXML private ValidationAutoComplete<DtoBankInformation> bankBlzComboBox;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationAutoComplete<Currency> cashAccountCurrencyComboBox;

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

        bankBlzComboBox.setConverter(dtoBankInformationBlzConverter);
        bankBicComboBox.setConverter(dtoBankInformationBicConverter);
        cashAccountCurrencyComboBox.setConverter(currencyConverter);
    }

    private ChangeListener<DtoBank> createBankChangeListener() {
        return (observable, oldValue, newValue) -> {
            boolean existingBankSelected = newValue.getId() != null;
            bankTitledPane.setDisable(existingBankSelected);

            if (existingBankSelected) {
                bankNameTextfield.setText(newValue.getName());
                bankBicComboBox.setText(newValue.getBic());
                bankBlzComboBox.setText(newValue.getBlz());
                logoButton.setImage(newValue.getLogo());
            } else {
                bankNameTextfield.clear();
                bankBicComboBox.clear();
                bankBlzComboBox.clear();
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
