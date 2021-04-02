package org.xmc.fe.stages.main.analysis.logic;

import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.analysis.AnalysisChartCalculationService;
import org.xmc.common.stubs.analysis.AnalysisType;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.fe.async.AsyncMonitor;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ChartDataForSelectedTypeLoadingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ChartDataForSelectedTypeLoadingController.class);
	
	private final AnalysisChartCalculationService analysisChartCalculationService;
	
	@Autowired
	public ChartDataForSelectedTypeLoadingController(AnalysisChartCalculationService analysisChartCalculationService) {
		this.analysisChartCalculationService = analysisChartCalculationService;
	}
	
	public <T> Optional<T> calculateChartForSelectedType(
			AsyncMonitor monitor,
			AnalysisType analysisType,
			Multimap<AssetType, Long> selectedAssetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		if (analysisType == null) {
			return Optional.empty();
		}
		
		switch (analysisType) {
			case ABSOLUTE_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAbsoluteAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate);
			case AGGREGATED_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAggregatedAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate);
			case ABSOLUTE_AND_AGGREGATED_ASSET_VALUE:
				return (Optional<T>)analysisChartCalculationService.calculateAbsoluteAndAggregatedAssetValueLineChart(
						monitor,
						selectedAssetIds,
						startDate,
						endDate);
			default:
				String message = String.format("Could not calculate chart for unknown analysis type '%s'.", analysisType);
				LOGGER.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
