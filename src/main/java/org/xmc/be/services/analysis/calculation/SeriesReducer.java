package org.xmc.be.services.analysis.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SeriesReducer {
	private static final double DEFAULT_EPSILON = 200.0;
	
	public <X, Y> List<DtoChartPoint<X, Y>> reduce(List<DtoChartPoint<X, Y>> points) {
		return reduce(points, DEFAULT_EPSILON);
	}
	
	private <X, Y> List<DtoChartPoint<X, Y>> reduce(List<DtoChartPoint<X, Y>> points, double epsilon) {
		if (CollectionUtils.isEmpty(points)) {
			return new ArrayList<>();
		}
		
		double furthestPointDistance = 0.0;
		int furthestPointIndex = 0;
		Line<X, Y> line = new Line<>(points.get(0), points.get(points.size() - 1));
		for (int i = 1; i < points.size() - 1; i++) {
			double distance = line.distance(points.get(i));
			if (distance > furthestPointDistance) {
				furthestPointDistance = distance;
				furthestPointIndex = i;
			}
		}
		
		if (furthestPointDistance > epsilon) {
			List<DtoChartPoint<X, Y>> reduced1 = reduce(points.subList(0, furthestPointIndex + 1), epsilon);
			List<DtoChartPoint<X, Y>> reduced2 = reduce(points.subList(furthestPointIndex, points.size()), epsilon);
			List<DtoChartPoint<X, Y>> result = new ArrayList<>(reduced1);
			result.addAll(reduced2.subList(1, reduced2.size()));
			return result;
		} else {
			return line.asList();
		}
	}
	
	private static class Line<X, Y> {
		private static final Logger LOGGER = LoggerFactory.getLogger(Line.class);
		
		private final DtoChartPoint<X, Y> start;
		private final DtoChartPoint<X, Y> end;
		
		private final double dx;
		private final double dy;
		private final double sxey;
		private final double exsy;
		private final double length;
		
		public Line(DtoChartPoint<X, Y> start, DtoChartPoint<X, Y> end) {
			this.start = start;
			this.end = end;
			dx = toDouble(start.getX()) - toDouble(end.getX());
			dy = toDouble(start.getY()) - toDouble(end.getY());
			sxey = toDouble(start.getX()) * toDouble(end.getY());
			exsy = toDouble(end.getX()) * toDouble(start.getY());
			length = Math.sqrt(dx * dx + dy * dy);
		}
		
		public List<DtoChartPoint<X, Y>> asList() {
			return Arrays.asList(start, end);
		}
		
		double distance(DtoChartPoint<X, Y> p) {
			return Math.abs(dy * toDouble(p.getX()) - dx * toDouble(p.getY()) + sxey - exsy) / length;
		}
		
		private double toDouble(Object value) {
			if (value == null) {
				return 0.0;
			} else if (value instanceof Number) {
				return ((Number) value).doubleValue();
			} else if (value instanceof LocalDate) {
				return ((LocalDate) value).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
			} else if (value instanceof LocalDateTime) {
				return ((LocalDateTime) value).toInstant(ZoneOffset.UTC).toEpochMilli();
			}
			
			String message = String.format("Cannot convert '%s' to double. Calculation not possible withpout valid double representation.", value);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
