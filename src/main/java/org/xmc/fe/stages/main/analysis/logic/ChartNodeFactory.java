package org.xmc.fe.stages.main.analysis.logic;

import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.AnalysisChartCalculationService;
import org.xmc.be.services.analysis.calculation.IncomeOutgoingPieChartCalculator;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.charts.ExtendedBarChart;
import org.xmc.fe.ui.charts.ExtendedLineChart;
import org.xmc.fe.ui.charts.ExtendedPieChart;
import org.xmc.fe.ui.charts.LocalDateTimeAxis;
import org.xmc.fe.ui.converter.GenericItemToStringConverter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class ChartNodeFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartNodeFactory.class);
	
	private final AsyncProcessor asyncProcessor;
	private final AnalysisChartCalculationService analysisChartCalculationService;
	
	@Autowired
	public ChartNodeFactory(AsyncProcessor asyncProcessor, AnalysisChartCalculationService analysisChartCalculationService) {
		this.asyncProcessor = asyncProcessor;
		this.analysisChartCalculationService = analysisChartCalculationService;
	}
	
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
				return createIncomeOutgoingPieChart((List<DtoChartSeries<Object, Number>>) result, analysisType);
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
	
	private ExtendedPieChart createIncomeOutgoingPieChart(List<DtoChartSeries<Object, Number>> result, AnalysisType analysisType) {
		ExtendedPieChart pieChart = new ExtendedPieChart();
		
		pieChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		pieChart.applyData(result);
		pieChart.setLegendVisible(false);
		
		for (int i = 0; i < pieChart.getData().size(); i++) {
			Data data = pieChart.getData().get(i);
			DtoChartSeries<Object, Number> serie = result.get(i);
			
			data.getNode().setOnMouseClicked(event -> {
				Long categoryId = (Long)serie.getParams().get(IncomeOutgoingPieChartCalculator.CATEGORY_ID);
				Collection<Long> cashAccountIds = (Collection<Long>)serie.getParams().get(IncomeOutgoingPieChartCalculator.CASHACCOUNT_IDS);
				LocalDate startDate = (LocalDate)serie.getParams().get(IncomeOutgoingPieChartCalculator.START_DATE);
				LocalDate endDate = (LocalDate)serie.getParams().get(IncomeOutgoingPieChartCalculator.END_DATE);
				
				if (analysisType == AnalysisType.INCOME) {
					asyncProcessor.runAsync(
							monitor -> analysisChartCalculationService.calculateIncomeForCategory(
									monitor, cashAccountIds, categoryId, startDate, endDate
							),
							pieChart::applyData
					);
				} else if (analysisType == AnalysisType.OUTGOING) {
					asyncProcessor.runAsync(
							monitor -> analysisChartCalculationService.calculateOutgoingForCategory(
									monitor, cashAccountIds, categoryId, startDate, endDate
							),
							pieChart::applyData
					);
				}
			});
		}
		
		return pieChart;
	}
}
