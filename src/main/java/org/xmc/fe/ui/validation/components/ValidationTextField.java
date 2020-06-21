package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.xmc.fe.ui.validation.IValidationComponent;

public class ValidationTextField extends TextField implements IValidationComponent {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;
    private String equalTo;
    private String customValidator;

    @Override
    public boolean validate() {
        return CommonTextfieldValidator.validate(this, CSS_CLASS_INVALID, required, minLength, maxLength, equalTo, customValidator);
    }

    @Override
    public void initValidationEvent(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getEqualTo() {
        return equalTo;
    }

    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    public String getCustomValidator() {
        return customValidator;
    }

    public void setCustomValidator(String customValidator) {
        this.customValidator = customValidator;
    }
}
