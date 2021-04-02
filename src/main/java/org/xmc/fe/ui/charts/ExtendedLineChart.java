package org.xmc.fe.ui.charts;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorConverter;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;

import java.util.List;

public class ExtendedLineChart<X, Y> extends AnchorPane {
	private final LineChart<X, Y> chart;
	private final Label mouseHoverLabel;
	
	private boolean showHoverLabel;
	
	public ExtendedLineChart(Axis<X> xAxis, Axis<Y> yAxis) {
		this.chart = new LineChart<>(xAxis, yAxis);
		this.chart.setCreateSymbols(false);
		
		this.mouseHoverLabel = new Label();
		
		this.getChildren().add(chart);
		AnchorPane.setBottomAnchor(chart, 0.0);
		AnchorPane.setLeftAnchor(chart, 0.0);
		AnchorPane.setRightAnchor(chart, 0.0);
		AnchorPane.setTopAnchor(chart, 0.0);
		
		this.getChildren().add(mouseHoverLabel);
		AnchorPane.setRightAnchor(mouseHoverLabel, 15.0);
		AnchorPane.setTopAnchor(mouseHoverLabel, 4.0);
	}
	
	public void applyData(List<DtoChartSeries<X, Y>> series) {
		chart.getData().clear();
		
		List<XYChart.Series<X, Y>> mappedSeries = new XYChartSeriesMapper().mapAll(chart, series);
		chart.getData().addAll(mappedSeries);
		
		applyChartLineColors(series, mappedSeries);
		applyTooltipClickListener();
	}
	
	private void applyChartLineColors(List<DtoChartSeries<X, Y>> series, List<XYChart.Series<X, Y>> mappedSeries) {
		String[] symbolStyles = new String[series.size()];
		
		for (int i = 0; i < series.size(); i++) {
			DtoChartSeries<X, Y> serie = series.get(i);
			XYChart.Series<X, Y> mappedSerie = mappedSeries.get(i);
			
			String color = StringColorConverter.convertColorToString(serie.getColor());
			
			Node line = mappedSerie.getNode().lookup(".chart-series-line");
			String lineStyle = "-fx-stroke: " + color + ";";
			line.setStyle(lineStyle);
			
			String symbolStyle = String.format("-fx-background-color: %s, whitesmoke;", color);
			symbolStyles[i] = symbolStyle;
			
			for (XYChart.Data<X, Y> data: mappedSerie.getData()) {
				Node symbolNode = data.getNode();
				if (symbolNode != null) {
					Node chartLineSymbol = symbolNode.lookup(".chart-line-symbol");
					if (chartLineSymbol != null) {
						chartLineSymbol.setStyle(symbolStyle);
					}
				}
				
				if (data.getNode() instanceof HoveredThresholdNode) {
					((HoveredThresholdNode) data.getNode()).getLabel().setStyle(symbolStyle + " " + lineStyle);
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
		Node chartBackground = this.lookup(".chart-plot-background");
		
		chartBackground.setOnMouseMoved(mouseEvent -> {
			if (!showHoverLabel) {
				return;
			}
			
			String x = calculateXValue(mouseEvent.getX());
			String y = calculateYValue(mouseEvent.getY());
			String message = MessageAdapter.getByKey(MessageKey.ANALYSIS_CHART_POINT_XY_HOVER, x, y);
			
			mouseHoverLabel.setText(message);
		});
	}
	
	private String calculateYValue(double mouseY) {
		Y yValue = chart.getYAxis().getValueForDisplay(mouseY);
		return calculateValue(chart.getYAxis(), yValue);
	}
	
	private String calculateXValue(double mouseX) {
		X xValue = chart.getXAxis().getValueForDisplay(mouseX);
		return calculateValue(chart.getXAxis(), xValue);
	}
	
	public static String calculateValue(Axis axis, Object value) {
		if (axis instanceof NumberAxis) {
			NumberAxis numberAxis = (NumberAxis) axis;
			if (numberAxis.getTickLabelFormatter() != null) {
				return numberAxis.getTickLabelFormatter().toString((Number)value);
			}
		}
		
		return value.toString();
	}
	
	public void setTitle(String title) {
		chart.setTitle(title);
	}
	
	public String getTitle() {
		return chart.getTitle();
	}
	
	public LineChart<X, Y> getChart() {
		return chart;
	}
	
	public Label getMouseHoverLabel() {
		return mouseHoverLabel;
	}
	
	public boolean isShowHoverLabel() {
		return showHoverLabel;
	}
	
	public void setShowHoverLabel(boolean showHoverLabel) {
		this.showHoverLabel = showHoverLabel;
	}
}
