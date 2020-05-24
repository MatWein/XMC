package org.xmc.fe.ui.validation.components;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ValidationScene;

import java.util.HashSet;
import java.util.Set;

class CommonTextfieldValidator {
    private static final Duration DELAY = Duration.millis(500);
    private static final String PREFIX = "- ";

    public static boolean validate(TextField textField, String cssClassInvalid, boolean required, int minLength, int maxLength) {
        String text = textField.getText();

        Set<String> errorMessages = new HashSet<>();

        if (required && StringUtils.isBlank(text)) {
            errorMessages.add(PREFIX + MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }
        if (text.length() < minLength) {
            errorMessages.add(PREFIX + MessageAdapter.getByKey(MessageKey.VALIDATION_MIN_LENGTH, minLength));
        }
        if (text.length() > maxLength) {
            errorMessages.add(PREFIX + MessageAdapter.getByKey(MessageKey.VALIDATION_MAX_LENGTH, maxLength));
        }

        boolean valid = errorMessages.isEmpty();
        if (valid) {
            textField.getStyleClass().removeAll(cssClassInvalid);
            textField.setTooltip(null);
        } else {
            textField.getStyleClass().add(cssClassInvalid);
            textField.setTooltip(new Tooltip(String.join(System.lineSeparator(), errorMessages)));
        }

        return valid;
    }

    public static void initValidationEvent(TextField textField, ValidationScene scene) {
        PauseTransition pause = new PauseTransition(DELAY);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> scene.validate());
            pause.playFromStart();
        });
    }
}
