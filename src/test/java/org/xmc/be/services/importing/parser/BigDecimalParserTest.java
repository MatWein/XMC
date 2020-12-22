package org.xmc.be.services.importing.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class BigDecimalParserTest {
	private BigDecimalParser parser;
	
	@BeforeEach
	void setUp() {
		parser = new BigDecimalParser();
	}
	
	@Test
	void parseBigDecimal() {
		Assertions.assertEquals(new BigDecimal("100"), parser.parseBigDecimal("100"));
		Assertions.assertEquals(new BigDecimal("100"), parser.parseBigDecimal(" 100  "));
		Assertions.assertEquals(new BigDecimal("100.11"), parser.parseBigDecimal("100.11"));
		Assertions.assertEquals(new BigDecimal("-100.11"), parser.parseBigDecimal("-100.11"));
		Assertions.assertEquals(new BigDecimal("-100.11"), parser.parseBigDecimal("- 100,11"));
		Assertions.assertEquals(new BigDecimal("100.11"), parser.parseBigDecimal("+ 100.11"));
		Assertions.assertEquals(new BigDecimal("0.99"), parser.parseBigDecimal("0,99"));
		Assertions.assertEquals(new BigDecimal("1001002.98"), parser.parseBigDecimal("1.001.002,98"));
		Assertions.assertEquals(new BigDecimal("1001002.98"), parser.parseBigDecimal("1,001,002.98"));
	}
	
	@Test
	void parseBigDecimal_Error() {
		Assertions.assertThrows(NumberFormatException.class, () -> parser.parseBigDecimal("100a"));
	}
	
	@Test
	void parseBigDecimal_Error_ReturnNull() {
		Assertions.assertNull(parser.parseBigDecimalNullOnError("100a"));
	}
}