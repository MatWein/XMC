package org.xmc.be.services.analysis.calculation;

import org.springframework.stereotype.Component;
import org.xmc.common.CommonConstants;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class AbsoluteAssetValueLineChartAggregator {
	public DtoChartSeries<LocalDateTime, Number> aggregate(
			List<DtoChartSeries<LocalDateTime, Number>> assetLines,
			LocalDate startDate,
			LocalDate endDate) {
		
		DtoChartSeries<LocalDateTime, Number> series = new DtoChartSeries<>();
		
		series.setName(MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_AGGREGATED_SERIES_NAME));
		series.setColor(Color.BLACK);
		series.setPoints(calculateAggregatedPoints(assetLines, startDate, endDate));
		
		return series;
	}
	
	private List<DtoChartPoint<LocalDateTime, Number>> calculateAggregatedPoints(
			List<DtoChartSeries<LocalDateTime, Number>> assetLines,
			LocalDate startDate,
			LocalDate endDate) {
		
		var result = new ArrayList<DtoChartPoint<LocalDateTime, Number>>();
		
		var currentDate = startDate;
		
		while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
			var assetPointsOnCurrentDateOrBefore = calculateAssetPointsOnCurrentDateOrBefore(assetLines, currentDate);
			double sum = assetPointsOnCurrentDateOrBefore.stream()
					.mapToDouble(point -> point.getY().doubleValue())
					.sum();
			
			result.add(createPoint(currentDate, sum));
			currentDate = currentDate.plusDays(1);
		}
		
		return result;
	}
	
	private DtoChartPoint<LocalDateTime, Number> createPoint(LocalDate currentDate, double sum) {
		DtoChartPoint<LocalDateTime, Number> point = new DtoChartPoint<>();
		
		point.setX(currentDate.atTime(CommonConstants.END_OF_DAY));
		point.setY(sum);
		
		String message = MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER,
				MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_AGGREGATED_SERIES_NAME),
				MessageAdapter.formatDate(point.getX().toLocalDate()),
				MessageAdapter.formatNumber(point.getY()));
		
		point.setMessage(message);
		
		return point;
	}
	
	private List<DtoChartPoint<LocalDateTime, Number>> calculateAssetPointsOnCurrentDateOrBefore(List<DtoChartSeries<LocalDateTime, Number>> assetLines, LocalDate currentDate) {
		var result = new ArrayList<DtoChartPoint<LocalDateTime, Number>>();
		
		for (DtoChartSeries<LocalDateTime, Number> asset : assetLines) {
			var assetPointOnCurrentDateOrBefore = findAssetPointOnCurrentDateOrBefore(asset, currentDate);
			if (assetPointOnCurrentDateOrBefore.isPresent()) {
				result.add(assetPointOnCurrentDateOrBefore.get());
			}
		}
		
		return result;
	}
	
	private Optional<DtoChartPoint<LocalDateTime, Number>> findAssetPointOnCurrentDateOrBefore(DtoChartSeries<LocalDateTime, Number> asset, LocalDate maxDate) {
		return asset.getPoints().stream()
				.filter((o) -> o.getX().toLocalDate().isBefore(maxDate) || o.getX().toLocalDate().isEqual(maxDate))
				.max(Comparator.comparing(DtoChartPoint::getX));
	}
}
