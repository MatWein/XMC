package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.validation.ICustomValidator;
import org.xmc.fe.ui.validation.IValidationComponent;

import java.util.HashSet;
import java.util.Set;

public class ValidationChoiceBox<T> extends ChoiceBox<T> implements IValidationComponent {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private String customValidator;

    @Override
    public boolean validate() {
        T selectedItem = this.getSelectionModel().getSelectedItem();

        Set<String> errorMessages = new HashSet<>();

        if (required && selectedItem == null) {
            errorMessages.add(CommonTextfieldValidator.PREFIX + MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }
        if (customValidator != null) {
            ICustomValidator<ChoiceBox<T>> validator = (ICustomValidator<ChoiceBox<T>>) ReflectionUtil.createNewInstanceFactory().call(ReflectionUtil.forName(customValidator));
            errorMessages.addAll(validator.validate(this));
        }

        boolean valid = errorMessages.isEmpty();
        if (valid) {
            this.getStyleClass().removeAll(CSS_CLASS_INVALID);
            this.setTooltip(null);
        } else {
            this.getStyleClass().add(CSS_CLASS_INVALID);
            this.setTooltip(new Tooltip(String.join(System.lineSeparator(), errorMessages)));
        }

        return valid;
    }

    @Override
    public void initValidationEvent(Scene scene) {
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> SceneUtil.getOrCreateValidationSceneState(scene).validate());
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCustomValidator() {
        return customValidator;
    }

    public void setCustomValidator(String customValidator) {
        this.customValidator = customValidator;
    }
}
