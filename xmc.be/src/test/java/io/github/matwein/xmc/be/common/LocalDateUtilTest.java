package io.github.matwein.xmc.be.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class LocalDateUtilTest {
	private static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2021, Month.APRIL, 4, 12, 13, 14);
	
	private LocalDateUtil localDateUtil;
	
	@BeforeEach
	void setUp() {
		localDateUtil = new LocalDateUtil();
	}
	
	@Test
	void testToMillis() {
		long result = localDateUtil.toMillis(LOCAL_DATE_TIME);
		
		Assertions.assertEquals(1617538394000L, result);
	}
	
	@Test
	void testToLocalDateTime() {
		LocalDateTime result = localDateUtil.toLocalDateTime(1617538394000L);
		
		Assertions.assertEquals(LOCAL_DATE_TIME, result);
	}
}