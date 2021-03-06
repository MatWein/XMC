package io.github.matwein.xmc.fe.ui.charts;

import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.fe.ui.TooltipBuilder;
import io.github.matwein.xmc.fe.ui.charts.mapper.PieChartSeriesMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;

import java.util.List;

public class ExtendedPieChart extends PieChart {
	private ChangeListener<Number> widthChangeListener;
	
	public void applyData(List<DtoChartSeries<Object, Number>> series) {
		getData().clear();
		
		List<Data> mappedSeries = new PieChartSeriesMapper().mapAll(series);
		getData().addAll(mappedSeries);
		
		for (int i = 0; i < getData().size(); i++) {
			Data data = getData().get(i);
			DtoChartSeries<Object, Number> serie = series.get(i);
			DtoChartPoint<Object, Number> point = serie.getPoints().get(0);
			
			Node node = data.getNode();
			Tooltip.install(node, TooltipBuilder.build(point.getDescription()));
		}
		
		if (widthChangeListener != null) {
			widthProperty().removeListener(widthChangeListener);
		}
		widthChangeListener = (obs, b, b1) -> applyChartLineColors(series);
		widthProperty().addListener(widthChangeListener);

		applyChartLineColors(series);
	}
	
	private void applyChartLineColors(List<DtoChartSeries<Object, Number>> series) {
		Platform.runLater(() -> {
			for (int i = 0; i < getData().size(); i++) {
				Data data = getData().get(i);
				DtoChartSeries<Object, Number> serie = series.get(i);
				
				String colorStyle = "-fx-pie-color: " + serie.getColor() + ";";
				data.getNode().setStyle(colorStyle);
				
				for (Node node : lookupAll(".default-color" + i)) {
					node.setStyle(colorStyle);
				}
			}
		});
	}
}
