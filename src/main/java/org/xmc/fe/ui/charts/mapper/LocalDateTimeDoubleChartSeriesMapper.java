package org.xmc.fe.ui.charts.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class LocalDateTimeDoubleChartSeriesMapper extends XYChartSeriesMapper {
	@Override
	protected Object convertXValue(Object originalValue) {
		LocalDateTime localDateTime = (LocalDateTime)originalValue;
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
}
