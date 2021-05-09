package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.services.analysis.AnalysisChartCalculationService;
import io.github.matwein.xmc.be.services.analysis.calculation.IncomeOutgoingPieChartCalculator;
import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.charts.ExtendedPieChart;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
public class IncomeOutgoingPieChartFactory {
	private final AsyncProcessor asyncProcessor;
	private final AnalysisChartCalculationService analysisChartCalculationService;
	
	@Autowired
	public IncomeOutgoingPieChartFactory(AsyncProcessor asyncProcessor, AnalysisChartCalculationService analysisChartCalculationService) {
		this.asyncProcessor = asyncProcessor;
		this.analysisChartCalculationService = analysisChartCalculationService;
	}
	
	public ExtendedPieChart createIncomeOutgoingPieChart(List<DtoChartSeries<Object, Number>> result, AnalysisType analysisType) {
		ExtendedPieChart pieChart = new ExtendedPieChart();
		
		pieChart.setTitle(MessageAdapter.getByKey(MessageKey.ANALYSIS_TYPE, analysisType));
		pieChart.setLegendVisible(false);
		pieChart.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				applyDataAndEvents(result, analysisType, pieChart);
			}
		});
		
		applyDataAndEvents(result, analysisType, pieChart);
		
		return pieChart;
	}
	
	private void applyDataAndEvents(List<DtoChartSeries<Object, Number>> result, AnalysisType analysisType, ExtendedPieChart pieChart) {
		pieChart.applyData(result);
		
		for (int i = 0; i < pieChart.getData().size(); i++) {
			PieChart.Data data = pieChart.getData().get(i);
			DtoChartSeries<Object, Number> serie = result.get(i);
			
			data.getNode().setOnMouseClicked(event -> onSliceClicked(analysisType, pieChart, serie));
		}
	}
	
	private void onSliceClicked(AnalysisType analysisType, ExtendedPieChart pieChart, DtoChartSeries<Object, Number> serie) {
		Long categoryId = (Long) serie.getParams().get(IncomeOutgoingPieChartCalculator.CATEGORY_ID);
		Collection<Long> cashAccountIds = (Collection<Long>) serie.getParams().get(IncomeOutgoingPieChartCalculator.CASHACCOUNT_IDS);
		LocalDate startDate = (LocalDate) serie.getParams().get(IncomeOutgoingPieChartCalculator.START_DATE);
		LocalDate endDate = (LocalDate) serie.getParams().get(IncomeOutgoingPieChartCalculator.END_DATE);
		
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
	}
}
