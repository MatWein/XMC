package io.github.matwein.xmc.fe.ui.validation.components;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.common.NumberUtils;
import io.github.matwein.xmc.fe.ui.components.FocusLostListener;
import io.github.matwein.xmc.fe.ui.components.IInitialFocus;
import io.github.matwein.xmc.fe.ui.validation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.LinkedHashSet;

public class ValidationNumberField extends TextField implements IValidationComponent, IRequired, IEqualTo, ICustomValidator, IMinMax, IInitialFocus {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private boolean initialFocus;
    private boolean zeroAllowed = true;
    private Double min;
    private Double max;
    private String equalTo;
    private String customValidator;
    private int fractionDigits = 2;
    private Integer integerDigits;

    public ValidationNumberField() {
        setAlignment(Pos.CENTER_RIGHT);
        setValue(0.0);
    }

    @Override
    public LinkedHashSet<String> validate() {
        LinkedHashSet<String> errors = CommonTextfieldValidator.validate(this);

        if (!zeroAllowed && getValue() == 0.0) {
            errors.add(MessageAdapter.getByKey(MessageKey.VALIDATION_ZERO_NOT_ALLOWED));
        }

        return errors;
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);

        this.focusedProperty().addListener(FocusLostListener.getInstance(this, () -> setValue(getValue())));

        if (initialFocus) {
            requestInitialFocus();
        }
    }

    public void setValue(double value) {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumFractionDigits(fractionDigits);
        numberInstance.setMaximumFractionDigits(fractionDigits);
        
        if (integerDigits != null) {
	        numberInstance.setMinimumIntegerDigits(integerDigits);
	        numberInstance.setMaximumIntegerDigits(integerDigits);
        }

        setText(numberInstance.format(value));
    }

    public void setValue(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.valueOf(0.0);
        }

        setValue(value.doubleValue());
    }

    public double getValue() {
        try {
            return BigDecimal.valueOf(NumberUtils.parseDoubleValue(getText())).setScale(fractionDigits, RoundingMode.DOWN).doubleValue();
        } catch (ParseException ignored) {
            return 0.0;
        }
    }

    public BigDecimal getValueAsBigDecimal() {
        return BigDecimal.valueOf(getValue());
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

        if (min != null && getValue() < min) {
            setValue(min);
        }
    }

    @Override
    public Double getMax() {
        return max;
    }

    @Override
    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public boolean isInitialFocus() {
        return initialFocus;
    }

    @Override
    public void setInitialFocus(boolean initialFocus) {
        this.initialFocus = initialFocus;
    }

    public boolean isZeroAllowed() {
        return zeroAllowed;
    }

    public void setZeroAllowed(boolean zeroAllowed) {
        this.zeroAllowed = zeroAllowed;
    }

    @Override
    public void addStyleClass(String styleClass) {
        getStyleClass().add(styleClass);
    }

    @Override
    public void removeStyleClass(String styleClass) {
        getStyleClass().removeAll(styleClass);
    }

    public int getFractionDigits() {
        return fractionDigits;
    }

    public void setFractionDigits(int fractionDigits) {
        this.fractionDigits = fractionDigits;
        setValue(getValue());
    }
	
	public Integer getIntegerDigits() {
		return integerDigits;
	}
	
	public void setIntegerDigits(Integer integerDigits) {
		this.integerDigits = integerDigits;
		setValue(getValue());
	}
}
