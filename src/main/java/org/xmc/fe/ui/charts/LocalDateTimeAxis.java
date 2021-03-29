package org.xmc.fe.ui.charts;

import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeAxis {
	public static Axis<LocalDateTime> createAxis() {
		NumberAxis axis = new NumberAxis();
		
		axis.setForceZeroInRange(false);
		
		axis.setTickLabelFormatter(GenericItemToStringConverter.getInstance(number -> {
			LocalDate localDate = Instant
					.ofEpochMilli(number.longValue())
					.atZone(ZoneOffset.UTC)
					.toLocalDate();
			
			return MessageAdapter.formatDate(localDate);
		}));
		
		return (Axis)axis;
	}
}
