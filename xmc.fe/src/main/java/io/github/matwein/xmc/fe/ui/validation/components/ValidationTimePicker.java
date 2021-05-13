package io.github.matwein.xmc.fe.ui.validation.components;

import io.github.matwein.xmc.fe.ui.components.IInitialFocus;
import io.github.matwein.xmc.fe.ui.validation.ICustomValidator;
import io.github.matwein.xmc.fe.ui.validation.IValidationComponent;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.time.LocalTime;
import java.util.LinkedHashSet;

public class ValidationTimePicker extends HBox implements IValidationComponent, ICustomValidator, IInitialFocus {
	private static final String CSS_CLASS_INVALID = "textfield-invalid";
	private static final double NUMBER_FIELD_WIDTH = 35.0;
	
	private final ValidationNumberField hourField;
	private final ValidationNumberField minuteField;
	private final ValidationNumberField secondField;
	
	private boolean required;
	private boolean initialFocus;
	private String customValidator;
	
	public ValidationTimePicker() {
		this.setAlignment(Pos.CENTER_LEFT);
		
		this.hourField = new ValidationNumberField();
		this.hourField.setFractionDigits(0);
		this.hourField.setIntegerDigits(2);
		this.hourField.setMin(0.0);
		this.hourField.setMax(24.0);
		this.hourField.setMaxWidth(NUMBER_FIELD_WIDTH);
		
		this.minuteField = new ValidationNumberField();
		this.minuteField.setFractionDigits(0);
		this.minuteField.setIntegerDigits(2);
		this.minuteField.setMin(0.0);
		this.minuteField.setMax(60.0);
		this.minuteField.setMaxWidth(NUMBER_FIELD_WIDTH);
		
		this.secondField = new ValidationNumberField();
		this.secondField.setFractionDigits(0);
		this.secondField.setIntegerDigits(2);
		this.secondField.setMin(0.0);
		this.secondField.setMax(60.0);
		this.secondField.setMaxWidth(NUMBER_FIELD_WIDTH);
		
		this.getChildren().addAll(
				hourField,
				new Label(" : "),
				minuteField,
				new Label(" : "),
				secondField);
		
		setValue(LocalTime.now());
	}
	
	public void setValue(LocalTime value) {
		if (value == null) {
			hourField.setValue(0);
			minuteField.setValue(0);
			secondField.setValue(0);
		} else {
			hourField.setValue(value.getHour());
			minuteField.setValue(value.getMinute());
			secondField.setValue(value.getSecond());
		}
	}
	
	public LocalTime getValue() {
		if (hourField.isValid() && minuteField.isValid() && secondField.isValid()) {
			return LocalTime.of((int)hourField.getValue(), (int)minuteField.getValue(), (int)secondField.getValue());
		}
		
		return null;
	}
	
	@Override
	public LinkedHashSet<String> validate() {
		LinkedHashSet<String> errorMessages = new LinkedHashSet<>();
		
		if (required && getValue() == null) {
			errorMessages.add(MessageAdapter.getByKey(MessageKey.VALIDATION_REQUIRED));
		}
		
		errorMessages.addAll(hourField.validate());
		errorMessages.addAll(minuteField.validate());
		errorMessages.addAll(secondField.validate());
		
		return errorMessages;
	}
	
	@Override
	public void initialize(Scene scene) {
		if (initialFocus) {
			requestInitialFocus();
		}
	}
	
	@Override
	public String getCssClassInvalid() {
		return CSS_CLASS_INVALID;
	}
	
	@Override
	public void setTooltip(Tooltip tooltip) {}
	
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
	
	@Override
	public void addStyleClass(String styleClass) {
		getStyleClass().add(styleClass);
	}
	
	@Override
	public void removeStyleClass(String styleClass) {
		getStyleClass().removeAll(styleClass);
	}
}
