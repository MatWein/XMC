package org.xmc.fe.ui.charts;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;

import java.util.List;

public class ExtendedLineChart<X, Y> extends LineChart<X, Y> {
	private boolean tooltipOnClick;
	
	public ExtendedLineChart(Axis<X> xAxis, Axis<Y> yAxis) {
		super(xAxis, yAxis);
	}
	
	public void applyData(List<DtoChartSeries<X, Y>> series) {
		this.getData().clear();
		
		List<XYChart.Series<X, Y>> mappedSeries = new XYChartSeriesMapper().mapAll(series);
		this.getData().addAll(mappedSeries);
		
		applyChartLineColors(series, mappedSeries);
		applyTooltipClickListener();
	}
	
	private void applyChartLineColors(List<DtoChartSeries<X, Y>> series, List<Series<X, Y>> mappedSeries) {
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
			
			for (Data<X, Y> data : mappedSerie.getData()) {
				if (getCreateSymbols()) {
					data.getNode().lookup(".chart-line-symbol").setStyle(symbolStyle);
				}
			}
		}
		
		Platform.runLater(() -> {
			for (Node node : this.lookupAll(".chart-legend-item-symbol")) {
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
	
	private void applyTooltipClickListener() {
		Axis<X> xAxis = this.getXAxis();
		Axis<Y> yAxis = this.getYAxis();
		
		Node chartBackground = this.lookup(".chart-plot-background");
		for (Node n : chartBackground.getParent().getChildrenUnmodifiable()) {
			if (n != chartBackground && n != xAxis && n != yAxis) {
				n.setMouseTransparent(true);
			}
		}
		
		Tooltip tooltip = new Tooltip();
		
		chartBackground.setOnMouseClicked(mouseEvent -> {
			if (!tooltipOnClick) {
				return;
			}
			
			X xValue = xAxis.getValueForDisplay(mouseEvent.getX());
			String x = calculateValue(xAxis, xValue);
			
			Y yValue = yAxis.getValueForDisplay(mouseEvent.getY());
			String y = calculateValue(yAxis, yValue);
			
			String message = MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER, x, y);
			
			tooltip.setText(message);
			tooltip.show(getScene().getWindow(), mouseEvent.getSceneX() + 20, mouseEvent.getSceneY() + 20);
		});
	}
	
	private String calculateValue(Axis axis, Object value) {
		if (axis instanceof NumberAxis) {
			NumberAxis numberAxis = (NumberAxis) axis;
			if (numberAxis.getTickLabelFormatter() != null) {
				return numberAxis.getTickLabelFormatter().toString((Number)value);
			}
		}
		
		return value.toString();
	}
	
	public boolean isTooltipOnClick() {
		return tooltipOnClick;
	}
	
	public void setTooltipOnClick(boolean tooltipOnClick) {
		this.tooltipOnClick = tooltipOnClick;
	}
}
