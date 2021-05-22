package io.github.matwein.xmc.be.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextToColorConverterTest {
	private TextToColorConverter textToColorConverter;
	
	@BeforeEach
	void setUp() {
		textToColorConverter = new TextToColorConverter();
	}
	
	@Test
	void testConvertTextToColor() {
		String result = textToColorConverter.convertTextToColor("some account name");
		
		Assertions.assertEquals("#776EBC", result);
	}
	
	@Test
	void testConvertTextToColor_Null() {
		String result = textToColorConverter.convertTextToColor(null);
		
		Assertions.assertNull(result);
	}
	
	@Test
	void testConvertTextToColor_Empty() {
		String result = textToColorConverter.convertTextToColor("");
		
		Assertions.assertEquals("#82298B", result);
	}
}