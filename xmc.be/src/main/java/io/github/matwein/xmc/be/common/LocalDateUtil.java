package io.github.matwein.xmc.be.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateUtil {
	public static long toMillis(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static LocalDate toLocalDate(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDate();
	}
	
	public static LocalDateTime toLocalDateTime(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDateTime();
	}
}