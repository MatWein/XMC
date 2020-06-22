package org.xmc.fe.stages.main.cashaccount;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javafx.fxml.FXML;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.IValidationController;
import org.xmc.fe.ui.validation.components.ValidationChoiceBox;
import org.xmc.fe.ui.validation.components.ValidationTextField;

@Component
public class CashAccountEditController implements IValidationController {
    @FXML private ValidationChoiceBox bankChoiceBox;
    @FXML private ValidationTextField bankNameTextfield;
    @FXML private ValidationTextField bankBicTextfield;
    @FXML private ValidationTextField bankBlzTextfield;
    @FXML private ValidationTextField cashAccountNameTextfield;
    @FXML private ValidationTextField cashAccountIbanTextfield;
    @FXML private ValidationTextField cashAccountNumberTextfield;
    @FXML private ValidationChoiceBox cashAccountCurrencyChoiceBox;

    @Override
    public Multimap<IValidationComponent, String> validate() {
        Multimap<IValidationComponent, String> validationErrors = ArrayListMultimap.create();

        if (bankChoiceBox.getSelectionModel().getSelectedItem() == null && StringUtils.isBlank(bankNameTextfield.getText())) {
            validationErrors.put(bankNameTextfield, MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }

        return validationErrors;
    }
}
