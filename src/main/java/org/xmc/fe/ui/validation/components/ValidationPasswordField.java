package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import org.xmc.fe.ui.validation.*;

import java.util.List;

public class ValidationPasswordField extends PasswordField implements IValidationComponent, IRequired, ILength, IEqualTo, ICustomValidator {
    private static final String CSS_CLASS_INVALID = "passwordfield-invalid";

    private boolean required;
    private Integer minLength;
    private Integer maxLength;
    private String equalTo;
    private String customValidator;

    @Override
    public List<String> validate() {
        return CommonTextfieldValidator.validate(this);
    }

    @Override
    public void initValidationEvent(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);
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
    public Integer getMinLength() {
        return minLength;
    }

    @Override
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getEqualTo() {
        return equalTo;
    }

    @Override
    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
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
