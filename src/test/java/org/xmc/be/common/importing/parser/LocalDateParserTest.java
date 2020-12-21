package org.xmc.be.common.importing.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;

class LocalDateParserTest {
	private LocalDateParser parser;
	
	@BeforeEach
	void setUp() {
		parser = new LocalDateParser();
	}
	
	@Test
	void parseDate() {
		Assertions.assertEquals(LocalDate.of(2000, Month.JANUARY, 1), parser.parseDate("01.01.2000"));
		Assertions.assertEquals(LocalDate.of(2000, Month.MAY, 26), parser.parseDate("26.05.2000 23:32:01"));
		Assertions.assertEquals(LocalDate.of(2021, Month.DECEMBER, 24), parser.parseDate("   24.12.2021  "));
		Assertions.assertEquals(LocalDate.of(2020, Month.AUGUST, 12), parser.parseDate("2020-08-12"));
	}
	
	@Test
	void parseDate_Error() {
		Assertions.assertThrows(DateTimeParseException.class, () -> parser.parseDate("01.01.20a00"));
	}
	
	@Test
	void parseDate_Error_ReturnNull() {
		Assertions.assertNull(parser.parseDateNullOnError("01.01.20a00"));
	}
}