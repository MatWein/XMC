package org.xmc.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

public class StringColorUtil {
	public static Color convertStringToAwtColor(String color) {
		if (color == null) {
			return null;
		}
		
		return Color.decode(color);
	}
	
	public static javafx.scene.paint.Color convertStringToColor(String color) {
		if (color == null) {
			return null;
		}
		
		return javafx.scene.paint.Color.valueOf(color);
	}
	
	public static String convertColorToString(javafx.scene.paint.Color color) {
		if (color == null) {
			return null;
		}
		
		return "#" + (format(color.getRed()) + format(color.getGreen()) + format(color.getBlue())).toUpperCase();
	}
	
	private static String format(double val) {
		String in = Integer.toHexString((int) Math.round(val * 255));
		return in.length() == 1 ? "0" + in : in;
	}
	
	public static String convertColorToString(Color color) {
		if (color == null) {
			return null;
		}
		
		return convertColorToString(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	private static String convertColorToString(int r, int g, int b) {
		return String.format("#%02x%02x%02x", r, g, b);
	}
	
	public static Color convertTextToColor(String text) {
		if (text == null) {
			return null;
		}
		
		String hex = convertTextToColorHex(text);
		return Color.decode(hex);
	}
	
	private static String convertTextToColorHex(String text) {
		String hex = String.format("#%X", StringUtils.rightPad(text, 10, '_').hashCode());
		return StringUtils.rightPad(hex, 7, '0').substring(0, 7);
	}
}
