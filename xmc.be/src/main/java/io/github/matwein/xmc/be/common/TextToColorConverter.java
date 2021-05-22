package io.github.matwein.xmc.be.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class TextToColorConverter {
	public String convertTextToColor(String text) {
		if (text == null) {
			return null;
		}
		
		return convertTextToColorHex(text);
	}
	
	private String convertTextToColorHex(String text) {
		String hex = String.format("#%X", StringUtils.rightPad(text, 10, '_').hashCode());
		return StringUtils.rightPad(hex, 7, '0').substring(0, 7);
	}
}
