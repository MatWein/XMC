package org.xmc.be.services.analysis;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.services.analysis.calculation.AbsoluteAssetValueLineChartAggregator;
import org.xmc.be.services.analysis.calculation.AbsoluteAssetValueLineChartCalculator;
import org.xmc.be.services.analysis.calculation.TransactionsBarChartCalculator;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AnalysisChartCalculationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisChartCalculationService.class);
	
	private final AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator;
	private final AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator;
	private final TransactionsBarChartCalculator transactionsBarChartCalculator;
	
	@Autowired
	public AnalysisChartCalculationService(
			AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator,
			AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator,
			TransactionsBarChartCalculator transactionsBarChartCalculator) {
		
		this.absoluteAssetValueLineChartCalculator = absoluteAssetValueLineChartCalculator;
		this.absoluteAssetValueLineChartAggregator = absoluteAssetValueLineChartAggregator;
		this.transactionsBarChartCalculator = transactionsBarChartCalculator;
	}
	
	public List<DtoChartSeries<Number, Number>> calculateAbsoluteAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		return absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
	}
	
	public List<DtoChartSeries<Number, Number>> calculateAggregatedAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		List<DtoChartSeries<Number, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		DtoChartSeries<Number, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines, startDate, endDate);
		
		return Lists.newArrayList(aggregatedLine);
	}
	
	public List<DtoChartSeries<Number, Number>> calculateAbsoluteAndAggregatedAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute and aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		List<DtoChartSeries<Number, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		
		if (assetLines.size() > 1) {
			DtoChartSeries<Number, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines, startDate, endDate);
			assetLines.add(aggregatedLine);
		}
		
		return assetLines;
	}
	
	public List<DtoChartSeries<String, Number>> calculateTransactionsBarChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating transactions bar chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		return transactionsBarChartCalculator.calculate(assetIds, startDate, endDate);
	}
}
