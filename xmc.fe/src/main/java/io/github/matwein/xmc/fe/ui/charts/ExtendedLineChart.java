package io.github.matwein.xmc.fe.ui.charts;

import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class ExtendedLineChart<X, Y> extends AnchorPane implements IChartBase<X, Y> {
	private final LineChart<X, Y> chart;
	private final Label mouseHoverLabel;
	
	private boolean showHoverLabel = true;
	private boolean showSymbols = true;
	private int maxHoverNodes = 30;
	private ChangeListener<Number> widthChangeListener;
	
	public ExtendedLineChart(Axis<X> xAxis, Axis<Y> yAxis) {
		this.chart = new LineChart<>(xAxis, yAxis);
		this.chart.setCreateSymbols(false);
		this.chart.getXAxis().setTickLabelRotation(90.0);
		
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
		
		List<XYChart.Series<X, Y>> mappedSeries = new XYChartSeriesMapper().mapAll(this, series);
		chart.getData().addAll(mappedSeries);
		
		if (widthChangeListener != null) {
			widthProperty().removeListener(widthChangeListener);
		}
		widthChangeListener = (obs, b, b1) -> applyChartLineColors(series);
		chart.widthProperty().addListener(widthChangeListener);
		
		applyChartLineColors(series);
		applyMouseMoveListener();
	}
	
	private void applyChartLineColors(List<DtoChartSeries<X, Y>> series) {
		Platform.runLater(() -> {
			for (int i = 0; i < series.size(); i++) {
				DtoChartSeries<X, Y> serie = series.get(i);
				
				String color = serie.getColor();
				
				StringBuilder styles = new StringBuilder();
				styles.append("-fx-stroke: ")
						.append(color)
						.append("; -fx-background-color: ")
						.append(color)
						.append(", whitesmoke;");
				
				for (Node node : chart.lookupAll(".default-color" + i)) {
					node.setStyle(styles.toString());
				}
				
				for (Node node : chart.lookupAll(".series" + i)) {
					node.setStyle(styles.toString());
				}
			}
		});
	}
	
	private void applyMouseMoveListener() {
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
	
	public void setLegendVisible(boolean legendVisible) {
		chart.setLegendVisible(legendVisible);
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
	
	@Override
	public boolean isShowHoverLabel() {
		return showHoverLabel;
	}
	
	public void setShowHoverLabel(boolean showHoverLabel) {
		this.showHoverLabel = showHoverLabel;
	}
	
	public boolean isShowSymbols() {
		return showSymbols;
	}
	
	public void setShowSymbols(boolean showSymbols) {
		this.showSymbols = showSymbols;
	}
	
	@Override
	public Axis<X> getXAxis() {
		return chart.getXAxis();
	}
	
	@Override
	public Axis<Y> getYAxis() {
		return chart.getYAxis();
	}
	
	@Override
	public int getMaxHoverNodes() {
		return maxHoverNodes;
	}
	
	public void setMaxHoverNodes(int maxHoverNodes) {
		this.maxHoverNodes = maxHoverNodes;
	}
}
