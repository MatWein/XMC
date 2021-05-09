package io.github.matwein.xmc.fe.ui.validation;

import com.google.common.collect.Lists;
import javafx.scene.control.TextField;
import nl.garvelink.iban.IBAN;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.ArrayList;
import java.util.Collection;

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
            return Lists.newArrayList(errorMessage);
        }
    }
}
