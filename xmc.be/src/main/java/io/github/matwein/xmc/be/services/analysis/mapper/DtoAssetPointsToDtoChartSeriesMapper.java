package io.github.matwein.xmc.be.services.analysis.mapper;

import io.github.matwein.xmc.be.common.TextToColorConverter;
import io.github.matwein.xmc.be.services.analysis.calculation.DuplicatedChartPointsReducer;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoAssetPointsToDtoChartSeriesMapper {
	private final DuplicatedChartPointsReducer duplicatedChartPointsReducer;
	
	@Autowired
	public DtoAssetPointsToDtoChartSeriesMapper(DuplicatedChartPointsReducer duplicatedChartPointsReducer) {
		this.duplicatedChartPointsReducer = duplicatedChartPointsReducer;
	}
	
	public List<DtoChartSeries<Number, Number>> mapAll(List<DtoAssetPoints> assetDeliveries, boolean filterDuplicatePoints) {
		return assetDeliveries.stream()
				.map(point -> calculateSerieFromDeliveries(point, filterDuplicatePoints))
				.filter(serie -> !serie.getPoints().isEmpty())
				.collect(Collectors.toList());
	}
	
	private DtoChartSeries<Number, Number> calculateSerieFromDeliveries(DtoAssetPoints dtoAssetPoints, boolean filterDuplicatePoints) {
		DtoChartSeries<Number, Number> calculatedSerie = new DtoChartSeries<>();
		
		calculatedSerie.setName(dtoAssetPoints.getAssetName());
		
		if (dtoAssetPoints.getAssetColor() == null) {
			calculatedSerie.setColor(TextToColorConverter.convertTextToColor(String.valueOf(dtoAssetPoints.getAssetId())));
		} else {
			calculatedSerie.setColor(dtoAssetPoints.getAssetColor());
		}
		
		List<DtoChartPoint<Number, Number>> points = dtoAssetPoints.getPoints();
		
		if (filterDuplicatePoints) {
			points = duplicatedChartPointsReducer.reduce(points);
		}
		
		calculatedSerie.setPoints(points);
		
		return calculatedSerie;
	}
}