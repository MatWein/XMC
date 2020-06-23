package org.xmc.fe.stages.main.cashaccount;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.DtoBank;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.stages.main.cashaccount.converter.DtoBankConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.IValidationController;
import org.xmc.fe.ui.validation.components.ValidationComboBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CashAccountEditController implements IValidationController {
    private final CurrencyConverter currencyConverter;
    private final DtoBankConverter dtoBankConverter;

    @FXML private TitledPane bankTitledPane;
    @FXML private ImageView bankLogoImageView;
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
            DtoBankConverter dtoBankConverter) {

        this.currencyConverter = currencyConverter;
        this.dtoBankConverter = dtoBankConverter;
    }

    @FXML
    public void initialize() {
        DtoBank emptyBank = new DtoBank();

        bankComboBox.setConverter(GenericItemToStringConverter.getInstance(dtoBankConverter));
        bankComboBox.getItems().add(emptyBank);
        bankComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> bankTitledPane.setDisable(newValue.getId() != null));
        bankComboBox.getSelectionModel().select(emptyBank);

        List<Currency> currencies = Currency.getAvailableCurrencies().stream()
                .sorted(Comparator.comparing(Currency::toString, Comparator.naturalOrder()))
                .collect(Collectors.toList());

        cashAccountCurrencyComboBox.setConverter(GenericItemToStringConverter.getInstance(currencyConverter));
        cashAccountCurrencyComboBox.getItems().addAll(currencies);
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
