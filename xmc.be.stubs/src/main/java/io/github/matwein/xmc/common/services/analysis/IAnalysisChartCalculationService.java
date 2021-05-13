package io.github.matwein.xmc.common.services.analysis;

import com.google.common.collect.Multimap;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface IAnalysisChartCalculationService {
	String CATEGORY_ID = "CATEGORY_ID";
	String CASHACCOUNT_IDS = "CASHACCOUNT_IDS";
	String START_DATE = "START_DATE";
	String END_DATE = "END_DATE";
	
	List<DtoChartSeries<Number, Number>> calculateAbsoluteAssetValueLineChart(
			IAsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Number, Number>> calculateAggregatedAssetValueLineChart(
			IAsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Number, Number>> calculateAbsoluteAndAggregatedAssetValueLineChart(
			IAsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<String, Number>> calculateTransactionsBarChart(
			IAsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Object, Number>> calculateIncome(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Object, Number>> calculateOutgoing(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Object, Number>> calculateIncomeForCategory(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			Long categoryId,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoChartSeries<Object, Number>> calculateOutgoingForCategory(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			Long categoryId,
			LocalDate startDate,
			LocalDate endDate);
	
	List<DtoMostRecentTransaction> calculateMostRecentTransactions(
			IAsyncMonitor monitor,
			Multimap<AssetType, Long> assetIds);
}
