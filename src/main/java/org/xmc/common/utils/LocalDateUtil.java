package org.xmc.common.utils;

import org.xmc.common.CommonConstants;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateUtil {
	public static long toMillis(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static long toMillisAtEndOfDay(LocalDate localDate) {
		return localDate.atTime(CommonConstants.END_OF_DAY).toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public static LocalDate toLocalDate(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDate();
	}
}
