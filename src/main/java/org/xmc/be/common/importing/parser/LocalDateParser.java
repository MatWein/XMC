package org.xmc.be.common.importing.parser;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component
public class LocalDateParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateParser.class);
	
	private static final LocalDate MIN_DATE = LocalDate.of(1900, Month.JANUARY, 1);
	private static final LocalDate MAX_DATE = LocalDate.of(2100, Month.JANUARY, 1);
	
	public LocalDate parseDateNullOnError(String value) {
		try {
			return parseDate(value);
		} catch (Throwable e) {
			LOGGER.warn("Error on parsing local date from value '{}': {}", value, e.getMessage());
			return null;
		}
	}
	
	public LocalDate parseDate(String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}
		
		value = value.trim();
		
		Optional<LocalDate> result = tryParseDate(value, "dd.MM.yyyy");
		if (isValidDate(result)) {
			return result.get();
		}
		
		result = tryParseDate(value, "dd/MM/yyyy");
		if (isValidDate(result)) {
			return result.get();
		}
		
		result = tryParseDateTime(value, "dd.MM.yyyy HH:mm:ss");
		if (isValidDate(result)) {
			return result.get();
		}
		
		result = tryParseDate(value, "yyyy-MM-dd");
		if (isValidDate(result)) {
			return result.get();
		}
		
		result = tryParseDateTime(value, "yyyy-MM-dd HH:mm:ss");
		if (isValidDate(result)) {
			return result.get();
		}
		
		String message = String.format("Could not parse local date from value '%s'.", value);
		throw new DateTimeParseException(message, value, 0);
	}
	
	private boolean isValidDate(Optional<LocalDate> result) {
		return result.isPresent()
				&& result.get().isAfter(MIN_DATE)
				&& result.get().isBefore(MAX_DATE);
	}
	
	public Optional<LocalDate> tryParseDate(String value, String format) {
		try {
			return Optional.of(LocalDate.parse(value, DateTimeFormatter.ofPattern(format)));
		} catch (Throwable e) {
			return Optional.empty();
		}
	}
	
	public Optional<LocalDate> tryParseDateTime(String value, String format) {
		try {
			return Optional.of(LocalDateTime.parse(value, DateTimeFormatter.ofPattern(format)).toLocalDate());
		} catch (Throwable e) {
			return Optional.empty();
		}
	}
}
