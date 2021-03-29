package org.xmc.fe.ui.charts;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorConverter;
import org.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;

import java.util.List;

public class ExtendedLineChart<X, Y> extends LineChart<X, Y> {
	public ExtendedLineChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
	}
	
	public void applyData(List<DtoChartSeries<X, Y>> series, XYChartSeriesMapper seriesMapper) {
		this.getData().clear();
		
		List<XYChart.Series<X, Y>> mappedSeries = seriesMapper.mapAll(series);
		this.getData().addAll(mappedSeries);
		
		String[] symbolStyles = new String[series.size()];
		
		for (int i = 0; i < series.size(); i++) {
			DtoChartSeries<X, Y> serie = series.get(i);
			Series<X, Y> mappedSerie = mappedSeries.get(i);
			
			String color = StringColorConverter.convertColorToString(serie.getColor());
			
			Node line = mappedSerie.getNode().lookup(".chart-series-line");
			String lineStyle = "-fx-stroke: " + color + ";";
			line.setStyle(lineStyle);
			
			String symbolStyle = String.format("-fx-background-color: %s, whitesmoke;", color);
			symbolStyles[i] = symbolStyle;
			
			for (XYChart.Data<X, Y> data: mappedSerie.getData()) {
				if (getCreateSymbols()) {
					data.getNode().lookup(".chart-line-symbol").setStyle(symbolStyle);
				}
				
				if (data.getNode() instanceof HoveredThresholdNode) {
					((HoveredThresholdNode) data.getNode()).getLabel().setStyle(symbolStyle + " " + lineStyle);
				}
			}
		}
		
		Platform.runLater(() -> {
			for (Node node: this.lookupAll(".chart-legend-item-symbol")) {
				for (String styleClass : node.getStyleClass()) {
					if (styleClass.startsWith("series")) {
						int i = Integer.parseInt(styleClass.substring(6));
						node.setStyle(symbolStyles[i]);
						break;
					}
				}
			}
		});
	}
}
