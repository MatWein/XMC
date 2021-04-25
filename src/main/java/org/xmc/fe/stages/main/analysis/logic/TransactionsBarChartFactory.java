package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedBarChart;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

import java.util.List;

@Component
public class TransactionsBarChartFactory {
	public ExtendedBarChart<String, Number> createTransactionsBarChart(List<DtoChartSeries<String, Number>> result, AnalysisType analysisType) {
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
}
