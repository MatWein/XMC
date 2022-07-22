package io.github.matwein.xmc.be.services.analysis;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.services.analysis.calculation.*;
import io.github.matwein.xmc.common.services.analysis.IAnalysisChartCalculationService;
import io.github.matwein.xmc.common.stubs.IAsyncMonitor;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
@Transactional
public class AnalysisChartCalculationService implements IAnalysisChartCalculationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalysisChartCalculationService.class);
	
	private static final Predicate<CashAccountTransaction> INCOME_FILTER = transaction -> transaction.getValue().doubleValue() > 0.0;
	private static final Predicate<CashAccountTransaction> OUTGOING_FILTER = transaction -> transaction.getValue().doubleValue() < 0.0;
	
	private final AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator;
	private final AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator;
	private final TransactionsBarChartCalculator transactionsBarChartCalculator;
	private final IncomeOutgoingPieChartCalculator incomeOutgoingPieChartCalculator;
	private final IncomeOutgoingForCategoryPieChartCalculator incomeOutgoingForCategoryPieChartCalculator;
	private final MostRecentTransactionsCalculator mostRecentTransactionsCalculator;
	
	@Autowired
	public AnalysisChartCalculationService(
			AbsoluteAssetValueLineChartCalculator absoluteAssetValueLineChartCalculator,
			AbsoluteAssetValueLineChartAggregator absoluteAssetValueLineChartAggregator,
			TransactionsBarChartCalculator transactionsBarChartCalculator,
			IncomeOutgoingPieChartCalculator incomeOutgoingPieChartCalculator,
			IncomeOutgoingForCategoryPieChartCalculator incomeOutgoingForCategoryPieChartCalculator,
			MostRecentTransactionsCalculator mostRecentTransactionsCalculator) {
		
		this.absoluteAssetValueLineChartCalculator = absoluteAssetValueLineChartCalculator;
		this.absoluteAssetValueLineChartAggregator = absoluteAssetValueLineChartAggregator;
		this.transactionsBarChartCalculator = transactionsBarChartCalculator;
		this.incomeOutgoingPieChartCalculator = incomeOutgoingPieChartCalculator;
		this.incomeOutgoingForCategoryPieChartCalculator = incomeOutgoingForCategoryPieChartCalculator;
		this.mostRecentTransactionsCalculator = mostRecentTransactionsCalculator;
	}
	
	@Override
	public List<DtoChartSeries<Number, Number>> calculateAbsoluteAssetValueLineChart(
			IAsyncMonitor monitor,
			Map<AssetType, List<Long>> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
	}
	
	@Override
	public List<DtoChartSeries<Number, Number>> calculateAggregatedAssetValueLineChart(
			IAsyncMonitor monitor,
			Map<AssetType, List<Long>> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		List<DtoChartSeries<Number, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		DtoChartSeries<Number, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines, startDate, endDate);
		
		return List.of(aggregatedLine);
	}
	
	@Override
	public List<DtoChartSeries<Number, Number>> calculateAbsoluteAndAggregatedAssetValueLineChart(
			IAsyncMonitor monitor,
			Map<AssetType, List<Long>> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating absolute and aggregated asset value line chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		List<DtoChartSeries<Number, Number>> assetLines = absoluteAssetValueLineChartCalculator.calculate(assetIds, startDate, endDate);
		
		if (assetLines.size() > 1) {
			DtoChartSeries<Number, Number> aggregatedLine = absoluteAssetValueLineChartAggregator.aggregate(assetLines, startDate, endDate);
			assetLines.add(aggregatedLine);
		}
		
		return assetLines;
	}
	
	@Override
	public List<DtoChartSeries<String, Number>> calculateTransactionsBarChart(
			IAsyncMonitor monitor,
			Map<AssetType, List<Long>> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating transactions bar chart for {}. Start date: {}. End date: {}", assetIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return transactionsBarChartCalculator.calculate(assetIds, startDate, endDate);
	}
	
	@Override
	public List<DtoChartSeries<Object, Number>> calculateIncome(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating income pie chart for cashaccounts {}. Start date: {}. End date: {}", cashAccountIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return incomeOutgoingPieChartCalculator.calculate(cashAccountIds, startDate, endDate, INCOME_FILTER);
	}
	
	@Override
	public List<DtoChartSeries<Object, Number>> calculateOutgoing(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating outgoing pie chart for cashaccounts {}. Start date: {}. End date: {}", cashAccountIds, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return incomeOutgoingPieChartCalculator.calculate(cashAccountIds, startDate, endDate, OUTGOING_FILTER);
	}
	
	@Override
	public List<DtoChartSeries<Object, Number>> calculateIncomeForCategory(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			Long categoryId,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating income pie chart for cashaccounts {} and category {}. Start date: {}. End date: {}",
				cashAccountIds, categoryId, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return incomeOutgoingForCategoryPieChartCalculator.calculate(cashAccountIds, categoryId, startDate, endDate, INCOME_FILTER);
	}
	
	@Override
	public List<DtoChartSeries<Object, Number>> calculateOutgoingForCategory(
			IAsyncMonitor monitor,
			Collection<Long> cashAccountIds,
			Long categoryId,
			LocalDate startDate,
			LocalDate endDate) {
		
		LOGGER.info("Calculating outgoing pie chart for cashaccounts {} and category {}. Start date: {}. End date: {}",
				cashAccountIds, categoryId, startDate, endDate);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return incomeOutgoingForCategoryPieChartCalculator.calculate(cashAccountIds, categoryId, startDate, endDate, OUTGOING_FILTER);
	}
	
	@Override
	public List<DtoMostRecentTransaction> calculateMostRecentTransactions(
			IAsyncMonitor monitor,
			Map<AssetType, List<Long>> assetIds) {
		
		LOGGER.info("Calculating most recent transactions for {}.", assetIds);
		monitor.setStatusText(MessageAdapter.getByKey(MessageKey.ASYNC_TASK_CALCULATING_CHART));
		
		return mostRecentTransactionsCalculator.calculate(assetIds);
	}
}
