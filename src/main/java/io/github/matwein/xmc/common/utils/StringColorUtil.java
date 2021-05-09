package io.github.matwein.xmc.common.utils;

import javafx.scene.paint.Color;
import org.apache.commons.lang3.StringUtils;

public class StringColorUtil {
	public static Color convertStringToColor(String color) {
		if (color == null) {
			return null;
		}
		
		return Color.valueOf(color);
	}
	
	public static String convertColorToString(Color color) {
		if (color == null) {
			return null;
		}
		
		return "#" + (format(color.getRed()) + format(color.getGreen()) + format(color.getBlue())).toUpperCase();
	}
	
	public static String convertTextToColor(String text) {
		if (text == null) {
			return null;
		}
		
		return convertTextToColorHex(text);
	}
	
	private static String format(double val) {
		String in = Integer.toHexString((int) Math.round(val * 255));
		return in.length() == 1 ? "0" + in : in;
	}
	
	private static String convertTextToColorHex(String text) {
		String hex = String.format("#%X", StringUtils.rightPad(text, 10, '_').hashCode());
		return StringUtils.rightPad(hex, 7, '0').substring(0, 7);
	}
}
