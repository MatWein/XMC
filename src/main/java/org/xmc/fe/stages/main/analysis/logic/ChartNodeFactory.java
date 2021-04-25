package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedBarChart;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.ExtendedPieChart;
import org.xmc.fe.ui.charts.LocalDateTimeAxis;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

import java.util.List;

@Component
public class ChartNodeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartNodeFactory.class);
	
	public <T> Node createChart(T result, AnalysisType analysisType) {
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
			case AGGREGATED_ASSET_VALUE:
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				return createAssetValueLineChart((List<DtoChartSeries<Number, Number>>) result, analysisType);
			case TRANSACTIONS:
				return createTransactionsBarChart((List<DtoChartSeries<String, Number>>) result, analysisType);
			case INCOME:
			case OUTGOING:
				return createPieChart((List<DtoChartSeries<Object, Number>>) result, analysisType);
			default:
				String message = String.format("Could not show chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
	
	private ExtendedBarChart<String, Number> createTransactionsBarChart(List<DtoChartSeries<String, Number>> result, AnalysisType analysisType) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_DATE));
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_VALUE_IN_EUR));
		yAxis.setTickLabelFormatter(GenericItemToStringConverter.getInstance(MessageAdapter::formatNumber));
		
		ExtendedBarChart<String, Number> barChart = new ExtendedBarChart<>(xAxis, yAxis);
		barChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		barChart.applyData(result);
		
		return barChart;
	}
	
	private ExtendedLineChart<Number, Number> createAssetValueLineChart(
			List<DtoChartSeries<Number, Number>> series,
			AnalysisType analysisType) {
		
		NumberAxis xAxis = LocalDateTimeAxis.createAxis();
		xAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_DATE));
		
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel(MessageAdapter.getByKey(MessageKey.ANALYSIS_AXIS_VALUE_IN_EUR));
		yAxis.setTickLabelFormatter(GenericItemToStringConverter.getInstance(MessageAdapter::formatNumber));
		
		ExtendedLineChart<Number, Number> lineChart = new ExtendedLineChart<>(xAxis, yAxis);
		lineChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		lineChart.setShowHoverLabel(true);
		lineChart.applyData(series);
		
		return lineChart;
	}
	
	private ExtendedPieChart createPieChart(List<DtoChartSeries<Object, Number>> result, AnalysisType analysisType) {
		ExtendedPieChart pieChart = new ExtendedPieChart();
		
		pieChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		pieChart.applyData(result);
		
		return pieChart;
	}
}
