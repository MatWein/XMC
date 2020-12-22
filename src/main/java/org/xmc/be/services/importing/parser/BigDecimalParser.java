package org.xmc.be.services.importing.parser;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BigDecimalParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(BigDecimalParser.class);
	
	public BigDecimal parseBigDecimalNullOnError(String value) {
		try {
			return parseBigDecimal(value);
		} catch (Throwable e) {
			LOGGER.warn("Error on parsing big decimal from value '{}': {}", value, e.getMessage());
			return null;
		}
	}
	
	public BigDecimal parseBigDecimal(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		value = StringUtils.deleteWhitespace(value);
		value = value.replace(',', '.');
		
		int indexOfLastDot = value.lastIndexOf('.');
		if (indexOfLastDot == -1) {
			return new BigDecimal(value);
		}
		
		value = value.substring(0, indexOfLastDot)
				.replace("." , "")
				.concat(value.substring(value.lastIndexOf(".")));
		
		return new BigDecimal(value);
	}
}
