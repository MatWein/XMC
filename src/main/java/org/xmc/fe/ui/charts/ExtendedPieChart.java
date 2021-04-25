package org.xmc.fe.ui.charts;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorUtil;
import org.xmc.fe.ui.charts.mapper.PieChartSeriesMapper;

import java.util.List;

public class ExtendedPieChart extends PieChart {
	public void applyData(List<DtoChartSeries<Object, Number>> series) {
		getData().clear();
		
		List<Data> mappedSeries = new PieChartSeriesMapper().mapAll(series);
		getData().addAll(mappedSeries);
		
		for (int i = 0; i < getData().size(); i++) {
			Data data = getData().get(i);
			DtoChartSeries<Object, Number> serie = series.get(i);
			DtoChartPoint<Object, Number> point = serie.getPoints().get(0);
			
			Tooltip.install(data.getNode(), new Tooltip(point.getDescription()));
		}
		
		widthProperty().addListener((obs, b, b1) -> applyChartLineColors(series));

		applyChartLineColors(series);
	}
	
	private void applyChartLineColors(List<DtoChartSeries<Object, Number>> series) {
		Platform.runLater(() -> {
			for (int i = 0; i < getData().size(); i++) {
				Data data = getData().get(i);
				DtoChartSeries<Object, Number> serie = series.get(i);
				
				String colorStyle = "-fx-pie-color: " + StringColorUtil.convertColorToString(serie.getColor()) + ";";
				data.getNode().setStyle(colorStyle);
				
				for (Node node : lookupAll(".default-color" + i)) {
					node.setStyle(colorStyle);
				}
			}
		});
	}
}
