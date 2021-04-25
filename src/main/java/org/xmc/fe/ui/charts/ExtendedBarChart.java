package org.xmc.fe.ui.charts;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExtendedBarChart<X, Y> extends AnchorPane implements IChartBase<X, Y> {
	private final BarChart<X, Y> chart;
	
	private boolean showHoverLabel = true;
	private int maxHoverNodes = 1000;
	
	public ExtendedBarChart(Axis<X> xAxis, Axis<Y> yAxis) {
		this.chart = new BarChart<>(xAxis, yAxis);
		
		this.getChildren().add(chart);
		AnchorPane.setBottomAnchor(chart, 0.0);
		AnchorPane.setLeftAnchor(chart, 0.0);
		AnchorPane.setRightAnchor(chart, 0.0);
		AnchorPane.setTopAnchor(chart, 0.0);
	}
	
	public void applyData(List<DtoChartSeries<X, Y>> series) {
		chart.getData().clear();
		
		List<XYChart.Series<X, Y>> mappedSeries = new XYChartSeriesMapper().mapAll(this, series);
		chart.getData().addAll(mappedSeries);
		
		CategoryAxis xAxis = (CategoryAxis) getXAxis();
		
		String dateTimeFormat = MessageAdapter.getByKey(MessageKey.APP_DATETIME_FORMAT);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
		
		List<String> categories = series.stream()
				.flatMap(serie -> serie.getPoints().stream())
				.map(DtoChartPoint::getX)
				.map(Objects::toString)
				.sorted(Comparator.comparing(value -> LocalDateTime.parse(value, formatter)))
				.collect(Collectors.toList());
		
		xAxis.setCategories(FXCollections.observableArrayList(categories));
		xAxis.setAutoRanging(true);
		
		chart.widthProperty().addListener((obs, b, b1) -> Platform.runLater(() -> {
			applyChartLineColors(series);
		}));
		
		applyChartLineColors(series);
	}
	
	private void applyChartLineColors(List<DtoChartSeries<X, Y>> series) {
		for (int i = 0; i < series.size(); i++) {
			DtoChartSeries<X, Y> serie = series.get(i);
			
			String color = StringColorUtil.convertColorToString(serie.getColor());
			
			StringBuilder styles = new StringBuilder();
			styles.append("-fx-bar-fill: ")
					.append(color)
					.append(";");
			
			for (Node node : chart.lookupAll(".default-color" + i + " .chart-bar")) {
				node.setStyle(styles.toString());
			}
			
			for (Node node : chart.lookupAll(".series" + i)) {
				node.setStyle(styles.toString());
			}
		}
	}
	
	public void setTitle(String title) {
		chart.setTitle(title);
	}
	
	public String getTitle() {
		return chart.getTitle();
	}
	
	public BarChart<X, Y> getChart() {
		return chart;
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
	public boolean isShowHoverLabel() {
		return showHoverLabel;
	}
	
	public void setShowHoverLabel(boolean showHoverLabel) {
		this.showHoverLabel = showHoverLabel;
	}
	
	@Override
	public int getMaxHoverNodes() {
		return maxHoverNodes;
	}
	
	public void setMaxHoverNodes(int maxHoverNodes) {
		this.maxHoverNodes = maxHoverNodes;
	}
}
