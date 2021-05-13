package io.github.matwein.xmc.fe.ui.validation;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;

@Component
public class CurrencyFieldValidator implements ICustomFieldValidator<TextField> {
    @Override
    public Collection<String> validate(TextField textField) {
        String text = textField.getText();
        if (StringUtils.isBlank(text)) {
            return new ArrayList<>(0);
        }

        try {
            Currency.getInstance(text);
            return new ArrayList<>(0);
        } catch (Throwable e) {
            String errorMessage = MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_CURRENCY);
            return Lists.newArrayList(errorMessage);
        }
    }
}
