package org.xmc.fe.ui.charts.mapper;

import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ChartSymbolHoverNode;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.IChartBase;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	public <X, Y> List<XYChart.Series<X, Y>> mapAll(IChartBase<X, Y> chart, List<DtoChartSeries<X, Y>> series) {
		return series.stream()
				.map(serie -> map(chart, serie))
				.collect(Collectors.toList());
	}
	
	private <X, Y> XYChart.Series<X, Y> map(IChartBase<X, Y> chart, DtoChartSeries<X, Y> series) {
		XYChart.Series<X, Y> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(chart, series));
		
		return mappedSeries;
	}
	
	private <X, Y> List<XYChart.Data<X, Y>> mapPoints(IChartBase<X, Y> chart, DtoChartSeries<X, Y> series) {
		List<XYChart.Data<X, Y>> points = series.getPoints().stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
		
		for (int i = 0; i < points.size(); i++) {
			int steps = points.size() / chart.getMaxHoverNodes();
			
			if (points.size() <= chart.getMaxHoverNodes() || i == 0 || i == points.size() - 1 || i % steps == 0) {
				XYChart.Data<X, Y> xyData = points.get(i);
				DtoChartPoint<X, Y> dtoChartPoint = series.getPoints().get(i);
				addHoverNodeToPoint(chart, xyData, dtoChartPoint, series);
			}
		}
		
		return points;
	}
	
	private <X, Y> void addHoverNodeToPoint(
			IChartBase<X, Y> chart,
			XYChart.Data<X, Y> xyData,
			DtoChartPoint<X, Y> dtoChartPoint,
			DtoChartSeries<X, Y> series) {
		
		String x = ExtendedLineChart.calculateValue(chart.getXAxis(), xyData.getXValue());
		String y = ExtendedLineChart.calculateValue(chart.getYAxis(), xyData.getYValue());
		
		StringBuilder message = new StringBuilder();
		
		message.append(series.getName())
				.append(System.lineSeparator())
				.append(MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER, x, y));
		
		if (dtoChartPoint.getDescription() != null) {
			message.append(System.lineSeparator()).append(dtoChartPoint.getDescription());
		}
		
		if (chart.isShowHoverLabel()) {
			xyData.setNode(new ChartSymbolHoverNode(message.toString()));
		}
	}
	
	private <X, Y> XYChart.Data<X, Y> mapPoint(DtoChartPoint<X, Y> point) {
		XYChart.Data<X, Y> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue(point.getX());
		mappedPoint.setYValue(point.getY());
		
		return mappedPoint;
	}
}
