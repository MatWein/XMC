package org.xmc.fe.ui.validation.components;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.xmc.fe.ui.components.IInitialFocus;
import org.xmc.fe.ui.validation.*;

import java.util.LinkedHashSet;

public class ValidationTextField extends TextField implements IValidationComponent, IRequired, ILength, IEqualTo, ICustomValidator, IInitialFocus {
	static final String CSS_CLASS_INVALID = "textfield-invalid";
	
	public static final int MAX_LENGTH = 255;
	
	private boolean required;
    private boolean initialFocus;
    private Integer minLength;
    private Integer maxLength = MAX_LENGTH;
    private String equalTo;
    private String customValidator;

    @Override
    public LinkedHashSet<String> validate() {
        return CommonTextfieldValidator.validate(this);
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);

        if (initialFocus) {
            requestInitialFocus();
        }
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
