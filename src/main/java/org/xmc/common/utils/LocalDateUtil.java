package org.xmc.common.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateUtil {
	public static long toMillis(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static long toMillis(LocalDate localDate) {
		return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static LocalDateTime toLocalDateTime(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDateTime();
	}
	
	public static LocalDate toLocalDate(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDate();
	}
}