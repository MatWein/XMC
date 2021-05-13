package io.github.matwein.xmc.fe.stages.main.analysis.logic;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.common.services.analysis.IAnalysisChartCalculationService;
import io.github.matwein.xmc.common.stubs.analysis.AnalysisType;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.fe.async.AsyncMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ChartDataForSelectedTypeLoadingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartDataForSelectedTypeLoadingController.class);
	
	private final IAnalysisChartCalculationService analysisChartCalculationService;
	
	@Autowired
	public ChartDataForSelectedTypeLoadingController(IAnalysisChartCalculationService analysisChartCalculationService) {
		this.analysisChartCalculationService = analysisChartCalculationService;
	}
	
	public <T> Optional<T> calculateChartForSelectedType(
			AsyncMonitor monitor,
			AnalysisType analysisType,
			Multimap<AssetType, Long> selectedAssetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		if (analysisType == null || selectedAssetIds.isEmpty() || startDate == null || endDate == null) {
			return Optional.empty();
		}
		
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
				return (Optional)Optional.of(analysisChartCalculationService.calculateAbsoluteAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate));
			case AGGREGATED_ASSET_VALUE:
				return (Optional)Optional.of(analysisChartCalculationService.calculateAggregatedAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate));
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				return (Optional)Optional.of(analysisChartCalculationService.calculateAbsoluteAndAggregatedAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate));
			case TRANSACTIONS:
				return (Optional)Optional.of(analysisChartCalculationService.calculateTransactionsBarChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate));
			case MOST_RECENT_TRANSACTIONS:
				return (Optional)Optional.of(analysisChartCalculationService.calculateMostRecentTransactions(
						monitor,
						selectedAssetIds));
			case INCOME:
				return (Optional)Optional.of(analysisChartCalculationService.calculateIncome(
						monitor,
						selectedAssetIds.get(AssetType.CASHACCOUNT),
						startDate,
						endDate));
			case OUTGOING:
				return (Optional)Optional.of(analysisChartCalculationService.calculateOutgoing(
						monitor,
						selectedAssetIds.get(AssetType.CASHACCOUNT),
						startDate,
						endDate));
			default:
				String message = String.format("Could not calculate chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
