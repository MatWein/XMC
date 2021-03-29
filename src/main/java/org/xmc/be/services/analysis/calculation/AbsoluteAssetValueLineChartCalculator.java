package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.controller.AssetDeliveriesLoadingController;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AbsoluteAssetValueLineChartCalculator {
	private final AssetDeliveriesLoadingController assetDeliveriesLoadingController;
	private final DuplicatedChartPointsReducer duplicatedChartPointsReducer;
	
	@Autowired
	public AbsoluteAssetValueLineChartCalculator(
			AssetDeliveriesLoadingController assetDeliveriesLoadingController,
			DuplicatedChartPointsReducer duplicatedChartPointsReducer) {
		
		this.assetDeliveriesLoadingController = assetDeliveriesLoadingController;
		this.duplicatedChartPointsReducer = duplicatedChartPointsReducer;
	}
	
	public List<DtoChartSeries<LocalDateTime, Number>> calculate(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetDeliveries> assetDeliveries = assetDeliveriesLoadingController.loadAssetDeliveries(assetIds, startDate, endDate);
		return calculateSeriesFromDeliveries(assetDeliveries);
	}
	
	private List<DtoChartSeries<LocalDateTime, Number>> calculateSeriesFromDeliveries(List<DtoAssetDeliveries> assetDeliveries) {
		return assetDeliveries.stream()
				.map(this::calculateSerieFromDeliveries)
				.filter(serie -> !serie.getPoints().isEmpty())
				.collect(Collectors.toList());
	}
	
	private DtoChartSeries<LocalDateTime, Number> calculateSerieFromDeliveries(DtoAssetDeliveries dtoAssetDeliveries) {
		DtoChartSeries<LocalDateTime, Number> calculatedSerie = new DtoChartSeries<>();
		
		calculatedSerie.setName(dtoAssetDeliveries.getAssetName());
		calculatedSerie.setColor(StringColorConverter.convertTextToColor(String.valueOf(dtoAssetDeliveries.getAssetId())));
		
		List<DtoChartPoint<LocalDateTime, Number>> points = calculatePoints(dtoAssetDeliveries);
		points = duplicatedChartPointsReducer.reduce(points);
		calculatedSerie.setPoints(points);
		
		return calculatedSerie;
	}
	
	private List<DtoChartPoint<LocalDateTime, Number>> calculatePoints(DtoAssetDeliveries dtoAssetDeliveries) {
		if (dtoAssetDeliveries.getDeliveries().isEmpty()) {
			return Lists.newArrayList();
		}
		
		return dtoAssetDeliveries.getDeliveries().stream()
				.map(delivery -> mapToPoint(delivery, dtoAssetDeliveries.getAssetName()))
				.collect(Collectors.toList());
	}
	
	private DtoChartPoint<LocalDateTime, Number> mapToPoint(Pair<LocalDateTime, Double> delivery, String name) {
		DtoChartPoint<LocalDateTime, Number> point = new DtoChartPoint<>();
		
		point.setX(delivery.getLeft());
		point.setY(delivery.getRight());
		
		String message = MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER,
				name,
				MessageAdapter.formatDate(point.getX().toLocalDate()),
				MessageAdapter.formatNumber(point.getY()));
		
		point.setMessage(message);
		
		return point;
	}
}
