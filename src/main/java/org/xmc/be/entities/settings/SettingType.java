package org.xmc.be.entities.settings;

public enum SettingType {
	EXTRAS_SHOW_SNOW (Boolean.class, true)
	;
	
	private final Class<?> valueType;
	private final Object defaultValue;
	
	<T> SettingType(Class<T> valueType, T defaultValue) {
		this.valueType = valueType;
		this.defaultValue = defaultValue;
	}
	
	public <T> Class<T> getValueType() {
		return (Class<T>)valueType;
	}
	
	public <T> T getDefaultValue() {
		return (T)defaultValue;
	}
}
