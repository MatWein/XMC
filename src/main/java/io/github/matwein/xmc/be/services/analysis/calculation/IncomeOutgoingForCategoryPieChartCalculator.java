package io.github.matwein.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.services.ccf.controller.AssetEuroValueCalculator;
import io.github.matwein.xmc.be.services.ccf.controller.CurrencyConversionFactorLoadingController;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import io.github.matwein.xmc.common.utils.StringColorUtil;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import scalc.SCalcBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IncomeOutgoingForCategoryPieChartCalculator {
	private static final double BORDER_TO_AGGREGATE_SMALL_TRANSACTIONS = 1.0;
	
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	private final UebertragTransactionFilter uebertragTransactionFilter;
	
	@Autowired
	public IncomeOutgoingForCategoryPieChartCalculator(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController,
			AssetEuroValueCalculator assetEuroValueCalculator,
			UebertragTransactionFilter uebertragTransactionFilter) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.currencyConversionFactorLoadingController = currencyConversionFactorLoadingController;
		this.assetEuroValueCalculator = assetEuroValueCalculator;
		this.uebertragTransactionFilter = uebertragTransactionFilter;
	}
	
	public List<DtoChartSeries<Object, Number>> calculate(
			Collection<Long> cashAccountIds,
			Long categoryId,
			LocalDate startDate,
			LocalDate endDate,
			Predicate<CashAccountTransaction> transactionPredicate) {
		
		List<CashAccount> cashAccounts = cashAccountJpaRepository.findAllById(cashAccountIds);
		
		Set<String> currencies = cashAccounts.stream()
				.map(CashAccount::getCurrency)
				.map(Currency::getCurrencyCode)
				.collect(Collectors.toSet());
		Multimap<String, CurrencyConversionFactor> currencyConversionFactors = currencyConversionFactorLoadingController.load(currencies);
		
		List<CashAccountTransaction> allTransactions = cashAccounts
				.stream()
				.flatMap(cashAccount -> cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount).stream())
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.collect(Collectors.toList());
		
		List<CashAccountTransaction> transactions = allTransactions.stream()
				.filter(transaction -> uebertragTransactionFilter.createNonUebertragPredicate(allTransactions).test(transaction))
				.filter(transactionPredicate)
				.filter(transaction -> transaction.getCategory() == null ? (categoryId == null) : transaction.getCategory().getId().equals(categoryId))
				.collect(Collectors.toList());
		
		BigDecimal sumOfAllTransactions = SCalcBuilder.bigDecimalInstance()
				.sumExpression()
				.build()
				.paramsAsCollection(transaction -> calculateTransactionValueInEuro(currencyConversionFactors, transaction), transactions)
				.calc();
		
		List<DtoChartSeries<Object, Number>> result = transactions.stream()
				.map(transaction -> mapEntry(currencyConversionFactors, transaction, sumOfAllTransactions))
				.collect(Collectors.toList());
		
		return aggregateSmallTransactions(sumOfAllTransactions, result);
	}
	
	private DtoChartSeries<Object, Number> mapEntry(
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors,
			CashAccountTransaction transaction,
			BigDecimal sumOfAllTransactions) {
		
		DtoChartSeries<Object, Number> result = new DtoChartSeries<>();
		
		String name = StringUtils.abbreviate(transaction.getUsage(), 15);
		
		result.setName(name);
		result.setColor(StringColorUtil.convertTextToColor(name));
		
		BigDecimal value = calculateTransactionValueInEuro(currencyConversionFactors, transaction);
		BigDecimal percentage = calculatePercentage(sumOfAllTransactions, value);
		
		String description = buildDescription(transaction.getUsage(), transaction.getValutaDate(), value, percentage);
		
		result.setPoints(Lists.newArrayList(new DtoChartPoint(name, value, description)));
		
		return result;
	}
	
	private String buildDescription(String description, LocalDate date, BigDecimal value, BigDecimal percentage) {
		return String.format(
				"%s: %s\n%s: %s %%\n%s: %s\n%s: %s",
				MessageAdapter.getByKey(MessageKey.ANALYSIS_SUM_IN_EUR),
				MessageAdapter.formatNumber(value),
				MessageAdapter.getByKey(MessageKey.ANALYSIS_PERCENTAGE),
				MessageAdapter.formatNumber(percentage),
				MessageAdapter.getByKey(MessageKey.ANALYSIS_DESCRIPTION),
				description,
				MessageAdapter.getByKey(MessageKey.ANALYSIS_DATE),
				StringUtils.defaultIfBlank(MessageAdapter.formatDate(date), "-")
		);
	}
	
	private BigDecimal calculatePercentage(BigDecimal sumOfAllTransactions, Number value) {
		return SCalcBuilder.bigDecimalInstance()
				.expression("value * 100 / sumOfAllTransactions")
				.build()
				.parameter("value", value)
				.parameter("sumOfAllTransactions", sumOfAllTransactions)
				.calc();
	}
	
	private BigDecimal calculateTransactionValueInEuro(
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors,
			CashAccountTransaction transaction) {
		
		BigDecimal value = assetEuroValueCalculator.calculateEuroValue(
				transaction.getValue(),
				transaction.getValutaDate().atStartOfDay(),
				transaction.getCashAccount().getCurrency(),
				currencyConversionFactors);
		
		return SCalcBuilder.bigDecimalInstance()
				.expression("abs(value)")
				.build()
				.parameter("value", value)
				.calc();
	}
	
	private List<DtoChartSeries<Object, Number>> aggregateSmallTransactions(
			BigDecimal sumOfAllTransactions,
			List<DtoChartSeries<Object, Number>> inputTransactions) {
		
		List<DtoChartSeries<Object, Number>> transactionsToKeep = Lists.newArrayListWithExpectedSize(inputTransactions.size());
		List<DtoChartSeries<Object, Number>> transactionsToAggregate = Lists.newArrayListWithExpectedSize(inputTransactions.size());
		
		for (DtoChartSeries<Object, Number> transaction : inputTransactions) {
			BigDecimal percentage = calculatePercentage(sumOfAllTransactions, transaction.getPoints().get(0).getY());
			
			if (percentage.doubleValue() < BORDER_TO_AGGREGATE_SMALL_TRANSACTIONS) {
				transactionsToAggregate.add(transaction);
			} else {
				transactionsToKeep.add(transaction);
			}
		}
		
		List<DtoChartSeries<Object, Number>> result = Lists.newArrayListWithExpectedSize(inputTransactions.size());
		result.addAll(transactionsToKeep);
		
		if (transactionsToAggregate.size() > 0) {
			DtoChartSeries<Object, Number> aggregatedSerie = new DtoChartSeries<>();
			
			String name = MessageAdapter.getByKey(MessageKey.ANALYSIS_OTHER);
			
			aggregatedSerie.setName(name);
			aggregatedSerie.setColor(StringColorUtil.convertTextToColor(name));
			
			BigDecimal sum = SCalcBuilder.bigDecimalInstance()
					.sumExpression()
					.build()
					.paramsAsCollection(transaction -> transaction.getPoints().get(0).getY(), transactionsToAggregate)
					.calc();
			
			BigDecimal percentage = calculatePercentage(sumOfAllTransactions, sum);
			
			String hint = MessageAdapter.getByKey(
					MessageKey.ANALYSIS_TRANSACTIONS_AGGREGATE_DESCRIPTION,
					MessageAdapter.formatNumber(BORDER_TO_AGGREGATE_SMALL_TRANSACTIONS)
			);
			
			String description = buildDescription(hint, null, sum, percentage);
			
			aggregatedSerie.setPoints(Lists.newArrayList(new DtoChartPoint(name, sum, description)));
			
			result.add(aggregatedSerie);
		}
		
		return result;
	}
}
