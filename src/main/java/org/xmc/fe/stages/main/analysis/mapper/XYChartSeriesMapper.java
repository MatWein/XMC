package org.xmc.fe.stages.main.analysis.mapper;

import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.HoveredThresholdNode;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	public List<XYChart.Series<Number, Number>> mapAll(List<DtoChartSeries<LocalDateTime, Number>> series) {
		return series.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private XYChart.Series<Number, Number> map(DtoChartSeries<LocalDateTime, Number> series) {
		XYChart.Series<Number, Number> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(series.getPoints()));
		
		return mappedSeries;
	}
	
	private List<XYChart.Data<Number, Number>> mapPoints(List<DtoChartPoint<LocalDateTime, Number>> points) {
		return points.stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
	}
	
	private XYChart.Data<Number, Number> mapPoint(DtoChartPoint<LocalDateTime, Number> point) {
		XYChart.Data<Number, Number> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue(point.getX().toInstant(ZoneOffset.UTC).toEpochMilli());
		mappedPoint.setYValue(point.getY());
		mappedPoint.setNode(new HoveredThresholdNode(
				MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER,
					MessageAdapter.formatDate(point.getX().toLocalDate()),
					MessageAdapter.formatNumber(point.getY())))
		);
		
		return mappedPoint;
	}
}
