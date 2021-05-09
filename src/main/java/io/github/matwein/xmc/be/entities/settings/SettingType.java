package io.github.matwein.xmc.be.entities.settings;

import java.io.Serializable;

public enum SettingType {
	EXTRAS_SHOW_SNOW (Boolean.class, true),
	DASHBOARD_CONFIG (String.class, null)
	;
	
	private final Class<?> valueType;
	private final Serializable defaultValue;
	
	<T extends Serializable> SettingType(Class<T> valueType, T defaultValue) {
		this.valueType = valueType;
		this.defaultValue = defaultValue;
	}
	
	public <T extends Serializable> Class<T> getValueType() {
		return (Class<T>)valueType;
	}
	
	public <T extends Serializable> T getDefaultValue() {
		return (T)defaultValue;
	}
}
