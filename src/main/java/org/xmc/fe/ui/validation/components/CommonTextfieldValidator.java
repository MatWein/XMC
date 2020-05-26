package org.xmc.fe.ui.validation.components;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ICustomValidator;
import org.xmc.fe.ui.validation.ValidationScene;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CommonTextfieldValidator {
    public static final String PREFIX = "- ";
    private static final Duration DELAY = Duration.millis(500);

    public static boolean validate(
            TextField textField,
            String cssClassInvalid,
            boolean required,
            int minLength,
            int maxLength,
            String equalTo,
            String customValidatorType) {

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
        if (equalTo != null) {
            ValidationScene validationScene = (ValidationScene)textField.getScene();
            Optional<TextField> otherTextfield = validationScene.getAllChildren(validationScene.getRoot()).stream()
                    .filter(node -> StringUtils.equals(equalTo, node.getId()))
                    .map(node -> (TextField)node)
                    .findAny();
            if (otherTextfield.isPresent() && !StringUtils.equals(text, otherTextfield.get().getText())) {
                errorMessages.add(PREFIX + MessageAdapter.getByKey(MessageKey.VALIDATION_NOT_EQUAL_TO, MessageAdapter.getByKey(MessageKey.PASSWORD)));
            }
        }
        if (customValidatorType != null) {
            ICustomValidator<TextField> validator = (ICustomValidator<TextField>)ReflectionUtil.createNewInstanceFactory().call(ReflectionUtil.forName(customValidatorType));
            errorMessages.addAll(validator.validate(textField));
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
