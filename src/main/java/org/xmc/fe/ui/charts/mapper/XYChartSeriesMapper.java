package org.xmc.fe.ui.charts.mapper;

import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ChartSymbolHoverNode;
import org.xmc.fe.ui.charts.ExtendedLineChart;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	private static final int MAX_NODE_AMOUNT = 30;
	
	public <X, Y> List<XYChart.Series<X, Y>> mapAll(ExtendedLineChart lineChart, List<DtoChartSeries<X, Y>> series) {
		return series.stream()
				.map(serie -> map(lineChart, serie))
				.collect(Collectors.toList());
	}
	
	private <X, Y> XYChart.Series<X, Y> map(ExtendedLineChart lineChart, DtoChartSeries<X, Y> series) {
		XYChart.Series<X, Y> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(lineChart, series));
		
		return mappedSeries;
	}
	
	private <X, Y> List<XYChart.Data<X, Y>> mapPoints(ExtendedLineChart lineChart, DtoChartSeries<X, Y> series) {
		List<XYChart.Data<X, Y>> points = series.getPoints().stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
		
		for (int i = 0; i < points.size(); i++) {
			int steps = points.size() / MAX_NODE_AMOUNT;
			
			if (points.size() <= MAX_NODE_AMOUNT || i == 0 || i == points.size() - 1 || i % steps == 0) {
				XYChart.Data<X, Y> xyData = points.get(i);
				addHoverNodeToPoint(lineChart, xyData, series.getName());
			}
		}
		
		return points;
	}
	
	private <X, Y> void addHoverNodeToPoint(ExtendedLineChart lineChart, XYChart.Data<X, Y> xyData, String name) {
		String x = ExtendedLineChart.calculateValue(lineChart.getChart().getXAxis(), xyData.getXValue());
		String y = ExtendedLineChart.calculateValue(lineChart.getChart().getYAxis(), xyData.getYValue());
		String message = name + System.lineSeparator() + MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER, x, y);
		
		if (lineChart.isShowSymbols()) {
			xyData.setNode(new ChartSymbolHoverNode(message));
		}
	}
	
	private <X, Y> XYChart.Data<X, Y> mapPoint(DtoChartPoint<X, Y> point) {
		XYChart.Data<X, Y> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue(point.getX());
		mappedPoint.setYValue(point.getY());
		
		return mappedPoint;
	}
}
