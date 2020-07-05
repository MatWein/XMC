package org.xmc.fe.ui.validation.components;

import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.validation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommonTextfieldValidator {
    public static List<String> validate(TextField textField) {
        List<String> errorMessages = new ArrayList<>();

        if (textField instanceof IRequired) {
            IRequired fieldWrapper = (IRequired) textField;
            boolean required = fieldWrapper.isRequired();

            if (required && StringUtils.isBlank(textField.getText())) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
            }
        }

        if (textField instanceof ILength) {
            ILength fieldWrapper = (ILength)textField;
            Integer minLength = fieldWrapper.getMinLength();
            Integer maxLength = fieldWrapper.getMaxLength();

            if (minLength != null && StringUtils.defaultString(textField.getText()).length() < minLength) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MIN_LENGTH, minLength));
            }
            if (maxLength != null && StringUtils.defaultString(textField.getText()).length() > maxLength) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MAX_LENGTH, maxLength));
            }
        }

        if (textField instanceof IEqualTo) {
            IEqualTo fieldWrapper = (IEqualTo)textField;
            String equalTo = fieldWrapper.getEqualTo();

            if (equalTo != null) {
                Optional<TextField> otherTextfield = SceneUtil.getAllChildren(textField.getScene().getRoot()).stream()
                        .filter(node -> StringUtils.equals(equalTo, node.getId()))
                        .map(node -> (TextField)node)
                        .findAny();
                if (otherTextfield.isPresent() && !StringUtils.equals(textField.getText(), otherTextfield.get().getText())) {
                    errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_NOT_EQUAL_TO, MessageAdapter.getByKey(MessageKey.PASSWORD)));
                }
            }
        }

        if (textField instanceof ICustomValidator) {
            ICustomValidator fieldWrapper = (ICustomValidator) textField;
            String customValidator = fieldWrapper.getCustomValidator();

            if (customValidator != null) {
                ICustomFieldValidator<TextField> validator = (ICustomFieldValidator<TextField>)ReflectionUtil.createNewInstanceFactory().call(ReflectionUtil.forName(customValidator));
                errorMessages.addAll(validator.validate(textField));
            }
        }

        return errorMessages;
    }

    public static void initValidationEvent(TextField textField, Scene scene) {
        PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);
        textField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            pause.setOnFinished(e -> SceneUtil.getOrCreateValidationSceneState(scene).validate());
            pause.playFromStart();
        });
    }
}
