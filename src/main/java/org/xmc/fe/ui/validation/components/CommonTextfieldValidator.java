package org.xmc.fe.ui.validation.components;

import javafx.animation.PauseTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;
import org.xmc.common.utils.NumberUtils;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.FeConstants;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.validation.*;

import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Function;

public class CommonTextfieldValidator {
    public static <COMPONENT_TYPE extends Parent> LinkedHashSet<String> validate(COMPONENT_TYPE component) {
        return validate(component, c -> (TextInputControl)c);
    }

    public static <COMPONENT_TYPE extends Parent> LinkedHashSet<String> validate(COMPONENT_TYPE component, Function<COMPONENT_TYPE, TextInputControl> textFieldExtractor) {
        LinkedHashSet<String> errorMessages = new LinkedHashSet<>();

        TextInputControl textfield = textFieldExtractor.apply(component);

        validateRequired(component, errorMessages, textfield);
        validateLength(component, errorMessages, textfield);
        validateMinMax(component, errorMessages, textfield);
        validateEqualTo(component, errorMessages, textfield);
        validateCustom(component, errorMessages);

        return errorMessages;
    }

    public static <COMPONENT_TYPE extends Parent> void validateRequired(
            COMPONENT_TYPE component,
            LinkedHashSet<String> errorMessages,
            TextInputControl textfield) {

        if (component instanceof IRequired) {
            IRequired fieldWrapper = (IRequired) component;
            boolean required = fieldWrapper.isRequired();

            if (required && StringUtils.isBlank(textfield.getText())) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
            }
        }
    }

    public static <COMPONENT_TYPE extends Parent> void validateLength(
            COMPONENT_TYPE component,
            LinkedHashSet<String> errorMessages,
            TextInputControl textfield) {

        if (component instanceof ILength) {
            ILength fieldWrapper = (ILength)component;
            Integer minLength = fieldWrapper.getMinLength();
            Integer maxLength = fieldWrapper.getMaxLength();

            if (minLength != null && StringUtils.defaultString(textfield.getText()).length() < minLength) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MIN_LENGTH, minLength));
            }
            if (maxLength != null && StringUtils.defaultString(textfield.getText()).length() > maxLength) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MAX_LENGTH, maxLength));
            }
        }
    }

    public static <COMPONENT_TYPE extends Parent> void validateMinMax(
            COMPONENT_TYPE component,
            LinkedHashSet<String> errorMessages,
            TextInputControl textfield) {

        if (component instanceof IMinMax) {
            IMinMax fieldWrapper = (IMinMax)component;
            Double min = fieldWrapper.getMin();
            Double max = fieldWrapper.getMax();

            try {
                double value = NumberUtils.parseDoubleValue(textfield.getText());

                if (min != null && value < min) {
                    errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MIN, min));
                }
                if (max != null && value > max) {
                    errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_MAX, max));
                }
            } catch (ParseException e) {
                errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_NUMBER_PARSE_ERROR));
            }
        }
    }

    public static <COMPONENT_TYPE extends Parent> void validateEqualTo(
            COMPONENT_TYPE component,
            LinkedHashSet<String> errorMessages,
            TextInputControl textfield) {

        if (component instanceof IEqualTo) {
            IEqualTo fieldWrapper = (IEqualTo)component;
            String equalTo = fieldWrapper.getEqualTo();

            if (equalTo != null) {
                Optional<TextField> otherTextfield = SceneUtil.getAllChildren(textfield.getScene().getRoot()).stream()
                        .filter(node -> StringUtils.equals(equalTo, node.getId()))
                        .map(node -> (TextField)node)
                        .findAny();
                if (otherTextfield.isPresent() && !StringUtils.equals(textfield.getText(), otherTextfield.get().getText())) {
                    errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_NOT_EQUAL_TO, MessageAdapter.getByKey(MessageKey.PASSWORD)));
                }
            }
        }
    }

    public static <COMPONENT_TYPE extends Parent> void validateCustom(COMPONENT_TYPE component, LinkedHashSet<String> errorMessages) {
        if (component instanceof ICustomValidator) {
            ICustomValidator fieldWrapper = (ICustomValidator) component;
            String customValidator = fieldWrapper.getCustomValidator();

            if (customValidator != null) {
                ICustomFieldValidator<COMPONENT_TYPE> validator = (ICustomFieldValidator<COMPONENT_TYPE>) ReflectionUtil
                        .createNewInstanceFactory()
                        .call(ReflectionUtil.forName(customValidator));

                errorMessages.addAll(validator.validate(component));
            }
        }
    }

    public static void initValidationEvent(TextInputControl textField, Scene scene) {
        PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);
        textField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            pause.setOnFinished(e -> SceneUtil.getOrCreateValidationSceneState(scene).validate());
            pause.playFromStart();
        });
    }
}
