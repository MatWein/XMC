package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.common.LocalDateUtil;
import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.common.CommonConstants;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class AbsoluteAssetValueLineChartAggregator {
	private final LocalDateUtil localDateUtil;
	
	@Autowired
	public AbsoluteAssetValueLineChartAggregator(LocalDateUtil localDateUtil) {
		this.localDateUtil = localDateUtil;
	}
	
	public DtoChartSeries<Number, Number> aggregate(
			List<DtoChartSeries<Number, Number>> assetLines,
			LocalDate startDate,
			LocalDate endDate) {
		
		DtoChartSeries<Number, Number> series = new DtoChartSeries<>();
		
		series.setName(MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_AGGREGATED_SERIES_NAME));
		series.setColor("#000000");
		series.setPoints(calculateAggregatedPoints(assetLines, startDate, endDate));
		
		return series;
	}
	
	private List<DtoChartPoint<Number, Number>> calculateAggregatedPoints(
			List<DtoChartSeries<Number, Number>> assetLines,
			LocalDate startDate,
			LocalDate endDate) {
		
		var result = new ArrayList<DtoChartPoint<Number, Number>>();
		
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
	
	private DtoChartPoint<Number, Number> createPoint(LocalDate currentDate, double sum) {
		DtoChartPoint<Number, Number> point = new DtoChartPoint<>();
		
		point.setX(localDateUtil.toMillis(currentDate.atTime(CommonConstants.END_OF_DAY)));
		point.setY(sum);
		
		return point;
	}
	
	private List<DtoChartPoint<Number, Number>> calculateAssetPointsOnCurrentDateOrBefore(List<DtoChartSeries<Number, Number>> assetLines, LocalDate currentDate) {
		var result = new ArrayList<DtoChartPoint<Number, Number>>();
		
		for (DtoChartSeries<Number, Number> asset : assetLines) {
			var assetPointOnCurrentDateOrBefore = findAssetPointOnCurrentDateOrBefore(asset, currentDate);
			if (assetPointOnCurrentDateOrBefore.isPresent()) {
				result.add(assetPointOnCurrentDateOrBefore.get());
			}
		}
		
		return result;
	}
	
	private Optional<DtoChartPoint<Number, Number>> findAssetPointOnCurrentDateOrBefore(DtoChartSeries<Number, Number> asset, LocalDate maxDate) {
		return asset.getPoints().stream()
				.filter((o) -> o.getX().longValue() <= localDateUtil.toMillis(maxDate.atTime(CommonConstants.END_OF_DAY)))
				.max(Comparator.comparing(point -> point.getX().longValue()));
	}
}
