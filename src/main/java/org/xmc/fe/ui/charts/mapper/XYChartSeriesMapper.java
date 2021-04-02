package org.xmc.fe.ui.charts.mapper;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.HoveredThresholdNode;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	private static final int MAX_NODE_DIFFERENCE = 15;
	
	public <X, Y> List<XYChart.Series<X, Y>> mapAll(LineChart lineChart, List<DtoChartSeries<X, Y>> series) {
		return series.stream()
				.map(serie -> map(lineChart, serie))
				.collect(Collectors.toList());
	}
	
	private <X, Y> XYChart.Series<X, Y> map(LineChart lineChart, DtoChartSeries<X, Y> series) {
		XYChart.Series<X, Y> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(lineChart, series));
		
		return mappedSeries;
	}
	
	private <X, Y> List<XYChart.Data<X, Y>> mapPoints(LineChart lineChart, DtoChartSeries<X, Y> series) {
		List<XYChart.Data<X, Y>> points = series.getPoints().stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
		
		for (int i = 0; i < points.size(); i++) {
			if (points.size() <= MAX_NODE_DIFFERENCE || i == 0 || i == points.size() - 1 || i % MAX_NODE_DIFFERENCE == 0) {
				XYChart.Data<X, Y> xyData = points.get(i);
				addHoverNodeToPoint(lineChart, xyData, series.getName());
			}
		}
		
		return points;
	}
	
	private <X, Y> void addHoverNodeToPoint(LineChart lineChart, XYChart.Data<X, Y> xyData, String name) {
		String x = ExtendedLineChart.calculateValue(lineChart.getXAxis(), xyData.getXValue());
		String y = ExtendedLineChart.calculateValue(lineChart.getYAxis(), xyData.getYValue());
		String message = name + System.lineSeparator() + MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER, x, y);
		
		xyData.setNode(new HoveredThresholdNode(message));
	}
	
	private <X, Y> XYChart.Data<X, Y> mapPoint(DtoChartPoint<X, Y> point) {
		XYChart.Data<X, Y> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue(point.getX());
		mappedPoint.setYValue(point.getY());
		
		return mappedPoint;
	}
}
