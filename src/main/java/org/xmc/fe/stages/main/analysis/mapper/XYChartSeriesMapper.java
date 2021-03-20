package org.xmc.fe.stages.main.analysis.mapper;

import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XYChartSeriesMapper {
	public List<XYChart.Series<String, Number>> mapAll(List<DtoChartSeries<LocalDate, Number>> series) {
		return series.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private XYChart.Series<String, Number> map(DtoChartSeries<LocalDate, Number> series) {
		XYChart.Series<String, Number> mappedSeries = new XYChart.Series<>();
		
		mappedSeries.setName(series.getName());
		mappedSeries.getData().addAll(mapPoints(series.getPoints()));
		
		return mappedSeries;
	}
	
	private List<XYChart.Data<String, Number>> mapPoints(List<DtoChartPoint<LocalDate, Number>> points) {
		return points.stream()
				.map(this::mapPoint)
				.collect(Collectors.toList());
	}
	
	private XYChart.Data<String, Number> mapPoint(DtoChartPoint<LocalDate, Number> point) {
		XYChart.Data<String, Number> mappedPoint = new XYChart.Data<>();
		
		mappedPoint.setXValue(MessageAdapter.formatDate(point.getX()));
		mappedPoint.setYValue(point.getY());
		
		return mappedPoint;
	}
}
