package org.xmc.be.services.importing.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeParseException;

class LocalDateTimeParserTest {
	private LocalDateTimeParser parser;
	
	@BeforeEach
	void setUp() {
		parser = new LocalDateTimeParser();
	}
	
	@Test
	void parseDate() {
		Assertions.assertEquals(LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0), parser.parseDateTime("01.01.2000"));
		Assertions.assertEquals(LocalDateTime.of(2000, Month.MAY, 26, 23, 32, 1), parser.parseDateTime("26.05.2000 23:32:01"));
		Assertions.assertEquals(LocalDateTime.of(2021, Month.DECEMBER, 24, 0, 0, 0), parser.parseDateTime("   24.12.2021  "));
		Assertions.assertEquals(LocalDateTime.of(2020, Month.AUGUST, 12, 0, 0, 0), parser.parseDateTime("2020-08-12"));
		Assertions.assertEquals(LocalDateTime.of(2020, Month.SEPTEMBER, 16, 0, 0, 0), parser.parseDateTime("16/09/2020"));
		Assertions.assertEquals(LocalDateTime.of(2020, Month.DECEMBER, 18, 8, 2, 40, 439180800), parser.parseDateTime("2020-12-18 08:02:40.439180800\n"));
	}
	
	@Test
	void parseDate_ExcelDate() {
		Assertions.assertEquals(LocalDateTime.of(2019, Month.SEPTEMBER, 11, 0, 0, 0), parser.parseDateTime("9/11/19"));
	}
	
	@Test
	void parseDate_Error() {
		Assertions.assertThrows(DateTimeParseException.class, () -> parser.parseDateTime("01.01.20a00"));
	}
	
	@Test
	void parseDate_Error_ReturnNull() {
		Assertions.assertNull(parser.parseDateTimeNullOnError("01.01.20a00"));
	}
}