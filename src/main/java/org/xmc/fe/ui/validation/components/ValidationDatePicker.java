package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import org.apache.commons.lang3.StringUtils;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.components.FocusLostListener;
import org.xmc.fe.ui.components.IInitialFocus;
import org.xmc.fe.ui.validation.ICustomValidator;
import org.xmc.fe.ui.validation.IValidationComponent;

import java.time.LocalDate;
import java.util.LinkedHashSet;

public class ValidationDatePicker extends DatePicker implements IValidationComponent, ICustomValidator, IInitialFocus {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private static final String VALID_DATE_CHARS = "0123456789./";

    private boolean required;
    private boolean initialFocus;
    private String customValidator;

    public ValidationDatePicker() {
        setValue(LocalDate.now());
    }

    @Override
    public LinkedHashSet<String> validate() {
        LinkedHashSet<String> errorMessages = new LinkedHashSet<>();

        if (required && getValue() == null) {
            errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
        }

        boolean containsInvalidChars = !StringUtils.containsOnly(getEditor().getText(), VALID_DATE_CHARS);
        if (containsInvalidChars) {
            errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_INVALID_DATE));
        }

        errorMessages.addAll(CommonTextfieldValidator.validate(this, ValidationDatePicker::getEditor));

        return errorMessages;
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(getEditor(), scene);
        this.valueProperty().addListener((observable, oldValue, newValue) -> SceneUtil.getOrCreateValidationSceneState(scene).validate());

        this.focusedProperty().addListener(FocusLostListener.getInstance(this, () -> {
            LocalDate currentValue = getValue();
            setValue(null);
            setValue(currentValue);
        }));

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

    @Override
    public boolean isInitialFocus() {
        return initialFocus;
    }

    @Override
    public void setInitialFocus(boolean initialFocus) {
        this.initialFocus = initialFocus;
    }
}
