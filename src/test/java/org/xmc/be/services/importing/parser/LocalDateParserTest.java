package org.xmc.be.services.importing.parser;

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
		Assertions.assertEquals(LocalDate.of(2020, Month.SEPTEMBER, 16), parser.parseDate("16/09/2020"));
		Assertions.assertEquals(LocalDate.of(2020, Month.DECEMBER, 18), parser.parseDate("2020-12-18 08:02:40.439180800\n"));
	}
	
	@Test
	void parseDate_ExcelDate() {
		Assertions.assertEquals(LocalDate.of(2019, Month.SEPTEMBER, 11), parser.parseDate("9/11/19"));
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