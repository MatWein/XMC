package io.github.matwein.xmc.fe.ui.validation.components;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.components.IInitialFocus;
import io.github.matwein.xmc.fe.ui.validation.ICustomValidator;
import io.github.matwein.xmc.fe.ui.validation.ILength;
import io.github.matwein.xmc.fe.ui.validation.IValidationComponent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import java.util.LinkedHashSet;

public class ValidationComboBox<T> extends ComboBox<T> implements IValidationComponent, ICustomValidator, ILength, IInitialFocus {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private boolean initialFocus;
    private Integer minLength;
    private Integer maxLength;
    private String customValidator;

    public ValidationComboBox() {
        setMaxWidth(Double.MAX_VALUE);
    }

    @Override
    public LinkedHashSet<String> validate() {
        T selectedItem = this.getSelectionModel().getSelectedItem();

        LinkedHashSet<String> errorMessages = new LinkedHashSet<>();

        if (required && selectedItem == null) {
            errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }

        errorMessages.addAll(CommonTextfieldValidator.validate(this, ValidationComboBox::getEditor));

        return errorMessages;
    }

    public void clear() {
        getSelectionModel().clearSelection();
        getEditor().setText(null);
    }

    @Override
    public void initialize(Scene scene) {
        this.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> SceneUtil.getOrCreateValidationSceneState(scene).validate());

        if (initialFocus) {
            requestInitialFocus();
        }
    }

    @Override
    public String getCssClassInvalid() {
        return CSS_CLASS_INVALID;
    }

    public boolean isRequired() {
        return required;
    }

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

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean isInitialFocus() {
        return initialFocus;
    }

    @Override
    public void setInitialFocus(boolean initialFocus) {
        this.initialFocus = initialFocus;
    }

    @Override
    public void addStyleClass(String styleClass) {
        getStyleClass().add(styleClass);
    }

    @Override
    public void removeStyleClass(String styleClass) {
        getStyleClass().removeAll(styleClass);
    }
}
