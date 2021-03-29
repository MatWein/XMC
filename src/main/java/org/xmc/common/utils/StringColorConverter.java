package org.xmc.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class StringColorConverter {
	public static Color convertTextToColor(String text) {
		String hex = convertTextToColorHex(text);
		return Color.decode(hex);
	}
	
	public static String convertColorToString(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
	
	private static String convertTextToColorHex(String text) {
		String hex = String.format("#%X", StringUtils.rightPad(text, 10, '_').hashCode());
		return StringUtils.rightPad(hex, 7, '0').substring(0, 7);
	}
}
