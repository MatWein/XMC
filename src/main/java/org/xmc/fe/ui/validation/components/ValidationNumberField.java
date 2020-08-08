package org.xmc.fe.ui.validation.components;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.xmc.common.utils.NumberUtils;
import org.xmc.fe.ui.validation.*;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedHashSet;

public class ValidationNumberField extends TextField implements IValidationComponent, IRequired, IEqualTo, ICustomValidator, IMinMax {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private Double min;
    private Double max;
    private String equalTo;
    private String customValidator;

    public ValidationNumberField() {
        setAlignment(Pos.CENTER_RIGHT);
        setText("0");
    }

    @Override
    public LinkedHashSet<String> validate() {
        return CommonTextfieldValidator.validate(this);
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);

        this.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (!Boolean.TRUE.equals(newPropertyValue)) {
                try {
                    double value = NumberUtils.parseDoubleValue(getText());
                    setText(NumberFormat.getNumberInstance().format(value));
                } catch (ParseException ignored) { }
            }
        });
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
    public Double getMin() {
        return min;
    }

    @Override
    public void setMin(Double min) {
        this.min = min;
    }

    @Override
    public Double getMax() {
        return max;
    }

    @Override
    public void setMax(Double max) {
        this.max = max;
    }
}