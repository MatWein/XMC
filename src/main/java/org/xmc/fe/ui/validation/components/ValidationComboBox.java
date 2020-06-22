package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import org.xmc.common.utils.ReflectionUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.validation.ICustomFieldValidator;
import org.xmc.fe.ui.validation.ICustomValidator;
import org.xmc.fe.ui.validation.IRequired;
import org.xmc.fe.ui.validation.IValidationComponent;

import java.util.ArrayList;
import java.util.List;

public class ValidationComboBox<T> extends ComboBox<T> implements IValidationComponent, IRequired, ICustomValidator {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private String customValidator;

    @Override
    public List<String> validate() {
        T selectedItem = this.getSelectionModel().getSelectedItem();

        List<String> errorMessages = new ArrayList<>();

        if (required && selectedItem == null) {
            errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }
        if (customValidator != null) {
            ICustomFieldValidator<ComboBox<T>> validator = (ICustomFieldValidator<ComboBox<T>>) ReflectionUtil.createNewInstanceFactory().call(ReflectionUtil.forName(customValidator));
            errorMessages.addAll(validator.validate(this));
        }

        return errorMessages;
    }

    @Override
    public void initValidationEvent(Scene scene) {
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> SceneUtil.getOrCreateValidationSceneState(scene).validate());
    }

    @Override
    public String getCssClassInvalid() {
        return CSS_CLASS_INVALID;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public String getCustomValidator() {
        return customValidator;
    }

    @Override
    public void setCustomValidator(String customValidator) {
        this.customValidator = customValidator;
    }
}
