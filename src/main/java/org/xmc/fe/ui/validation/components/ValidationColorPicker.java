package org.xmc.fe.ui.validation.components;

import javafx.scene.control.ColorPicker;
import org.xmc.common.utils.StringColorUtil;

public class ValidationColorPicker extends ColorPicker {
	private static final double HEIGHT = 30.0;
	
	public ValidationColorPicker() {
		this.setValue(null);
		this.setMaxWidth(Double.MAX_VALUE);
		this.setMinHeight(HEIGHT);
		this.setPrefHeight(HEIGHT);
		this.setMaxHeight(HEIGHT);
	}
	
	public String getValueHex() {
		if (getValue() == null) {
			return null;
		}
		
		return StringColorUtil.convertColorToString(getValue());
	}
	
	public void setValueHex(String color) {
		setValue(StringColorUtil.convertStringToColor(color));
	}
}
