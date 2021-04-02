package org.xmc.be.services.analysis.mapper;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.calculation.DuplicatedChartPointsReducer;
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorUtil;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoAssetPointsToDtoChartSeriesMapper {
	private final DuplicatedChartPointsReducer duplicatedChartPointsReducer;
	
	@Autowired
	public DtoAssetPointsToDtoChartSeriesMapper(DuplicatedChartPointsReducer duplicatedChartPointsReducer) {
		this.duplicatedChartPointsReducer = duplicatedChartPointsReducer;
	}
	
	public List<DtoChartSeries<Number, Number>> mapAll(List<DtoAssetPoints> assetDeliveries) {
		return assetDeliveries.stream()
				.map(this::calculateSerieFromDeliveries)
				.filter(serie -> !serie.getPoints().isEmpty())
				.collect(Collectors.toList());
	}
	
	private DtoChartSeries<Number, Number> calculateSerieFromDeliveries(DtoAssetPoints dtoAssetPoints) {
		DtoChartSeries<Number, Number> calculatedSerie = new DtoChartSeries<>();
		
		calculatedSerie.setName(dtoAssetPoints.getAssetName());
		
		if (dtoAssetPoints.getAssetColor() == null) {
			calculatedSerie.setColor(StringColorUtil.convertTextToColor(String.valueOf(dtoAssetPoints.getAssetId())));
		} else {
			calculatedSerie.setColor(StringColorUtil.convertStringToAwtColor(dtoAssetPoints.getAssetColor()));
		}
		
		List<DtoChartPoint<Number, Number>> points = calculatePoints(dtoAssetPoints);
		points = duplicatedChartPointsReducer.reduce(points);
		
		calculatedSerie.setPoints(points);
		
		return calculatedSerie;
	}
	
	private List<DtoChartPoint<Number, Number>> calculatePoints(DtoAssetPoints dtoAssetPoints) {
		if (dtoAssetPoints.getPoints().isEmpty()) {
			return Lists.newArrayList();
		}
		
		return dtoAssetPoints.getPoints().stream()
				.map(this::mapToPoint)
				.collect(Collectors.toList());
	}
	
	private DtoChartPoint<Number, Number> mapToPoint(Pair<Number, Number> delivery) {
		DtoChartPoint<Number, Number> point = new DtoChartPoint<>();
		
		point.setX(delivery.getLeft());
		point.setY(delivery.getRight());
		
		return point;
	}
}
