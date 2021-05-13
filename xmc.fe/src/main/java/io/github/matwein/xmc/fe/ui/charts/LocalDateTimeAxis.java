package io.github.matwein.xmc.fe.ui.charts;

import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import javafx.scene.chart.NumberAxis;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateTimeAxis {
	public static NumberAxis createAxis() {
		NumberAxis axis = new NumberAxis();
		
		axis.setForceZeroInRange(false);
		
		axis.setTickLabelFormatter(GenericItemToStringConverter.getInstance(number -> {
			LocalDate localDate = Instant
					.ofEpochMilli(number.longValue())
					.atZone(ZoneOffset.UTC)
					.toLocalDate();
			
			return MessageAdapter.formatDate(localDate);
		}));
		
		return axis;
	}
}
