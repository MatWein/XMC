package io.github.matwein.xmc.fe.common;

import javafx.scene.paint.Color;

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
	
	private static String format(double val) {
		String in = Integer.toHexString((int) Math.round(val * 255));
		return in.length() == 1 ? "0" + in : in;
	}
}
