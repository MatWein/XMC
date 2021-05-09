package io.github.matwein.xmc.be.services.settings.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.settings.SettingType;

import java.io.Serializable;

@Component
public class SettingValueCaster {
	public <T extends Serializable> T castToType(SettingType settingType, String value) {
		Class<Serializable> valueType = settingType.getValueType();
		
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		if (valueType.equals(String.class)) {
			return (T)value;
		} else if (valueType.equals(Boolean.class)) {
			return (T)Boolean.valueOf(value);
		}
		
		String message = String.format("Could not cast setting value '%s' to unknown type '%s'.", value, valueType.getName());
		throw new IllegalArgumentException(message);
	}
	
	public <T extends Serializable> String castToString(SettingType settingType, T value) {
		Class<Serializable> valueType = settingType.getValueType();
		
		if (value == null) {
			return null;
		}
		
		if (valueType.equals(String.class)) {
			return (String)value;
		} else if (valueType.equals(Boolean.class) || valueType.equals(boolean.class)) {
			return value.toString();
		}
		
		String message = String.format("Could not cast value '%s' to unknown setting type '%s'.", value, valueType.getName());
		throw new IllegalArgumentException(message);
	}
}
