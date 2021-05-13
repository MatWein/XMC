package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.fe.ui.charts.ExtendedBarChart;
import io.github.matwein.xmc.fe.ui.converter.GenericItemToStringConverter;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import org.springframework.stereotype.Component;

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
