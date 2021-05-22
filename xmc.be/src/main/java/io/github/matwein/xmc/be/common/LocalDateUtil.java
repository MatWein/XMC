package io.github.matwein.xmc.be.common;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class LocalDateUtil {
	public long toMillis(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public LocalDateTime toLocalDateTime(Number value) {
		return Instant
				.ofEpochMilli(value.longValue())
				.atZone(ZoneOffset.UTC)
				.toLocalDateTime();
	}
}
