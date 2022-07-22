package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.common.MessageAdapter;
import io.github.matwein.xmc.be.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.be.common.TextToColorConverter;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.depot.CurrencyConversionFactor;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.services.ccf.controller.AssetEuroValueCalculator;
import io.github.matwein.xmc.be.services.ccf.controller.CurrencyConversionFactorLoadingController;
import io.github.matwein.xmc.common.services.analysis.IAnalysisChartCalculationService;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import scalc.SCalcBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IncomeOutgoingPieChartCalculator {
	private static final String ABS_SUM = "abs(sum(ALL_PARAMS))";
	
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	private final UebertragTransactionFilter uebertragTransactionFilter;
	private final TextToColorConverter textToColorConverter;
	
	@Autowired
	public IncomeOutgoingPieChartCalculator(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController,
			AssetEuroValueCalculator assetEuroValueCalculator,
			UebertragTransactionFilter uebertragTransactionFilter,
			TextToColorConverter textToColorConverter) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.currencyConversionFactorLoadingController = currencyConversionFactorLoadingController;
		this.assetEuroValueCalculator = assetEuroValueCalculator;
		this.uebertragTransactionFilter = uebertragTransactionFilter;
		this.textToColorConverter = textToColorConverter;
	}
	
	public List<DtoChartSeries<Object, Number>> calculate(
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate,
			Predicate<CashAccountTransaction> transactionPredicate) {
		
		List<CashAccount> cashAccounts = cashAccountJpaRepository.findAllById(cashAccountIds);
		
		Set<String> currencies = cashAccounts.stream()
				.map(CashAccount::getCurrency)
				.collect(Collectors.toSet());
		Map<String, List<CurrencyConversionFactor>> currencyConversionFactors = currencyConversionFactorLoadingController.load(currencies);
		
		List<CashAccountTransaction> allTransactions = cashAccounts
				.stream()
				.flatMap(cashAccount -> cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount).stream())
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.collect(Collectors.toList());
		
		List<CashAccountTransaction> transactions = allTransactions
				.stream()
				.filter(transaction -> uebertragTransactionFilter.createNonUebertragPredicate(allTransactions).test(transaction))
				.filter(transactionPredicate)
				.collect(Collectors.toList());
		
		Map<Pair<Long, String>, List<CashAccountTransaction>> transactionsPerCategory = transactions.stream()
				.collect(Collectors.groupingBy(this::extractCategoryName));
		
		BigDecimal sumOfAllTransactions = SCalcBuilder.bigDecimalInstance()
				.expression(ABS_SUM)
				.build()
				.paramsAsCollection(transaction -> calculateTransactionValueInEuro(currencyConversionFactors, transaction), transactions)
				.calc();
		
		return transactionsPerCategory.entrySet().stream()
				.map(entry -> mapEntry(cashAccountIds, startDate, endDate, currencyConversionFactors, entry, sumOfAllTransactions))
				.collect(Collectors.toList());
	}
	
	private Pair<Long, String> extractCategoryName(CashAccountTransaction transaction) {
		if (transaction.getCategory() == null) {
			return ImmutablePair.of(null, MessageAdapter.getByKey(MessageKey.ANALYSIS_OTHER));
		} else {
			return ImmutablePair.of(transaction.getCategory().getId(), transaction.getCategory().getName());
		}
	}
	
	private DtoChartSeries<Object, Number> mapEntry(
			Collection<Long> cashAccountIds,
			LocalDate startDate,
			LocalDate endDate,
			Map<String, List<CurrencyConversionFactor>> currencyConversionFactors,
			Entry<Pair<Long, String>, List<CashAccountTransaction>> entry,
			BigDecimal sumOfAllTransactions) {
		
		DtoChartSeries<Object, Number> result = new DtoChartSeries<>();
		
		Pair<Long, String> group = entry.getKey();
		String categoryName = group.getRight();
		
		result.setName(categoryName);
		result.getParams().put(IAnalysisChartCalculationService.CATEGORY_ID, entry.getKey().getLeft());
		result.getParams().put(IAnalysisChartCalculationService.CASHACCOUNT_IDS, cashAccountIds);
		result.getParams().put(IAnalysisChartCalculationService.START_DATE, startDate);
		result.getParams().put(IAnalysisChartCalculationService.END_DATE, endDate);
		
		result.setColor(textToColorConverter.convertTextToColor(categoryName));
		
		BigDecimal sum = SCalcBuilder.bigDecimalInstance()
				.expression(ABS_SUM)
				.build()
				.paramsAsCollection(transaction -> calculateTransactionValueInEuro(currencyConversionFactors, transaction), entry.getValue())
				.calc();
		
		BigDecimal percentage = SCalcBuilder.bigDecimalInstance()
				.expression("sum * 100 / sumOfAllTransactions")
				.build()
				.parameter("sum", sum)
				.parameter("sumOfAllTransactions", sumOfAllTransactions)
				.calc();
		
		String description = String.format(
				"%s: %s\n%s: %s %%",
				MessageAdapter.getByKey(MessageKey.ANALYSIS_SUM_IN_EUR),
				MessageAdapter.formatNumber(sum),
				MessageAdapter.getByKey(MessageKey.ANALYSIS_PERCENTAGE),
				MessageAdapter.formatNumber(percentage)
		);
		
		result.setPoints(List.of(new DtoChartPoint<>(categoryName, sum, description)));
		
		return result;
	}
	
	private BigDecimal calculateTransactionValueInEuro(
			Map<String, List<CurrencyConversionFactor>> currencyConversionFactors,
			CashAccountTransaction transaction) {
		
		return assetEuroValueCalculator.calculateEuroValue(
				transaction.getValue(),
				transaction.getValutaDate().atStartOfDay(),
				transaction.getCashAccount().getCurrency(),
				currencyConversionFactors);
	}
}
