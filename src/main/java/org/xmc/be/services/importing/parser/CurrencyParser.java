package org.xmc.be.services.importing.parser;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Currency;

@Component
public class CurrencyParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyParser.class);
	
	public Currency parseCurrencyNullOnError(String value) {
		try {
			return parseCurrency(value);
		} catch (Throwable e) {
			LOGGER.warn("Error on parsing currency from value '{}': {}", value, e.getMessage());
			return null;
		}
	}
	
	public Currency parseCurrency(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		value = value.trim();
		
		return Currency.getInstance(value);
	}
}
