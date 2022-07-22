package io.github.matwein.xmc.fe.ui.validation;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import javafx.scene.control.TextField;
import nl.garvelink.iban.IBAN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class IbanFieldValidator implements ICustomFieldValidator<TextField> {
    @Override
    public Collection<String> validate(TextField textField) {
        String text = textField.getText();
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>(0);
        }

        try {
            IBAN.parse(text);
            return new ArrayList<>(0);
        } catch (Throwable e) {
            String errorMessage = MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_IBAN);
            return List.of(errorMessage);
        }
    }
}
