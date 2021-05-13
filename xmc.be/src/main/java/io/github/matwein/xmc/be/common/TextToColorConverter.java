package io.github.matwein.xmc.be.common;

import org.apache.commons.lang3.StringUtils;

public class TextToColorConverter {
	public static String convertTextToColor(String text) {
		if (text == null) {
			return null;
		}
		
		return convertTextToColorHex(text);
	}
	
	private static String convertTextToColorHex(String text) {
		String hex = String.format("#%X", StringUtils.rightPad(text, 10, '_').hashCode());
		return StringUtils.rightPad(hex, 7, '0').substring(0, 7);
	}
}
