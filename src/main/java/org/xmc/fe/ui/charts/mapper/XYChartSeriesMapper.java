package org.xmc.fe.ui.charts.mapper;

import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;

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
		
		mappedPoint.setXValue(point.getX());
		mappedPoint.setYValue(point.getY());
		
		return mappedPoint;
	}
}
