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
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.fe.async.AsyncMonitor;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnalysisChartCalculationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisChartCalculationService.class);
	
	private final AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator;
	private final AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator;
	
	@Autowired
	public AnalysisChartCalculationService(
			AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator,
			AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator) {
		
		this.absoluteAssetValueLineChartCalculator = absoluteAssetValueLineChartCalculator;
		this.absoluteAssetValueLineChartAggregator = absoluteAssetValueLineChartAggregator;
	}
	
	public Optional<List<DtoChartSeries<LocalDate, Number>>> calculateAbsoluteAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		if (assetIds.isEmpty() || startDate == null || endDate == null) {
			return Optional.empty();
		}
		
		return Optional.ofNullable(absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate));
	}
	
	public Optional<List<DtoChartSeries<LocalDate, Number>>> calculateAggregatedAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		if (assetIds.isEmpty() || startDate == null || endDate == null) {
			return Optional.empty();
		}
		
		List<DtoChartSeries<LocalDate, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		DtoChartSeries<LocalDate, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines);
		
		return Optional.of(Lists.newArrayList(aggregatedLine));
	}
	
	public Optional<List<DtoChartSeries<LocalDate, Number>>> calculateAbsoluteAndAggregatedAssetValueLineChart(
			AsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute and aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageKey.ASYNC_TASK_CALCULATING_CHART);
		
		if (assetIds.isEmpty() || startDate == null || endDate == null) {
			return Optional.empty();
		}
		
		List<DtoChartSeries<LocalDate, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		
		DtoChartSeries<LocalDate, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines);
		assetLines.add(aggregatedLine);
		
		return Optional.of(assetLines);
	}
}
