package io.github.matwein.xmc.be.services.settings.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.be.entities.settings.SettingType;

class SettingValueCasterTest {
	private SettingValueCaster caster;
	
	@BeforeEach
	void setUp() {
		caster = new SettingValueCaster();
	}
	
	@Test
	void testCastToType_Boolean() {
		boolean result = caster.castToType(SettingType.EXTRAS_SHOW_SNOW, "true");
		
		Assertions.assertTrue(result);
	}
	
	@Test
	void testCastToType_BooleanFalse() {
		boolean result = caster.castToType(SettingType.EXTRAS_SHOW_SNOW, "false");
		
		Assertions.assertFalse(result);
	}
	
	@Test
	void testCastToType_BooleanNull() {
		Boolean result = caster.castToType(SettingType.EXTRAS_SHOW_SNOW, null);
		
		Assertions.assertNull(result);
	}
	
	@Test
	void testCastToType_BooleanNull_EmptyString() {
		Boolean result = caster.castToType(SettingType.EXTRAS_SHOW_SNOW, "");
		
		Assertions.assertNull(result);
	}
	
	@Test
	void testCastToString() {
		String result = caster.castToString(SettingType.EXTRAS_SHOW_SNOW, true);
		
		Assertions.assertEquals("true", result);
	}
	
	@Test
	void testCastToString_False() {
		String result = caster.castToString(SettingType.EXTRAS_SHOW_SNOW, false);
		
		Assertions.assertEquals("false", result);
	}
	
	@Test
	void testCastToString_Null() {
		String result = caster.castToString(SettingType.EXTRAS_SHOW_SNOW, null);
		
		Assertions.assertNull(result);
	}
}