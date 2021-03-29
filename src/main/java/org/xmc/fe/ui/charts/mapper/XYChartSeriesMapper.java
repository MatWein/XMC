package org.xmc.fe.ui.charts.mapper;

import javafx.scene.chart.XYChart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.charts.HoveredThresholdNode;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	public <X, Y> List<XYChart.Series<X, Y>> mapAll(List<DtoChartSeries<X, Y>> series) {
		return series.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private <X, Y> XYChart.Series<X, Y> map(DtoChartSeries<X, Y> series) {
		XYChart.Series<X, Y> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(series));
		
		return mappedSeries;
	}
	
	private <X, Y> List<XYChart.Data<X, Y>> mapPoints(DtoChartSeries<X, Y> series) {
		return series.getPoints().stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
	}
	
	private <X, Y> XYChart.Data<X, Y> mapPoint(DtoChartPoint<X, Y> point) {
		XYChart.Data<X, Y> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue((X)convertXValue(point.getX()));
		mappedPoint.setYValue((Y)convertYValue(point.getY()));
		mappedPoint.setNode(new HoveredThresholdNode(StringUtils.defaultString(point.getMessage())));
		
		return mappedPoint;
	}
	
	protected Object convertXValue(Object originalValue) { return originalValue; }
	protected Object convertYValue(Object originalValue) { return originalValue; }
}
