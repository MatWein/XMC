package org.xmc.fe.ui.charts;

import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorUtil;
import org.xmc.fe.ui.charts.mapper.XYChartSeriesMapper;

import java.util.List;

public class ExtendedBarChart<X, Y> extends AnchorPane implements IChartBase<X, Y> {
	private final BarChart<X, Y> chart;
	
	private boolean showHoverLabel = true;
	
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
		
		applyChartLineColors(series, mappedSeries);
	}
	
	private void applyChartLineColors(List<DtoChartSeries<X, Y>> series, List<XYChart.Series<X, Y>> mappedSeries) {
		String[] symbolStyles = new String[series.size()];
		
		for (int i = 0; i < series.size(); i++) {
			DtoChartSeries<X, Y> serie = series.get(i);
			XYChart.Series<X, Y> mappedSerie = mappedSeries.get(i);
			
			String color = StringColorUtil.convertColorToString(serie.getColor());
			
			String lineStyle = "-fx-stroke: " + color + ";";

			String symbolStyle = String.format("-fx-background-color: %s, whitesmoke;", color);
			symbolStyles[i] = symbolStyle;

			for (XYChart.Data<X, Y> data: mappedSerie.getData()) {
				if (data.getNode() instanceof ChartSymbolHoverNode) {
					((ChartSymbolHoverNode) data.getNode()).getTooltip().setStyle(symbolStyle + " " + lineStyle);
				}
			}
		}
		
//		Platform.runLater(() -> {
//			for (Node node : this.lookupAll(".chart-legend-item-symbol")) {
//				for (String styleClass : node.getStyleClass()) {
//					if (styleClass.startsWith("series")) {
//						int i = Integer.parseInt(styleClass.substring(6));
//						node.setStyle(symbolStyles[i]);
//						break;
//					}
//				}
//			}
//		});
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
}
