package io.github.matwein.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IncomeOutgoingPieChartCalculator {
	public static final String CATEGORY_ID = "CATEGORY_ID";
	public static final String CASHACCOUNT_IDS = "CASHACCOUNT_IDS";
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	
	private static final String ABS_SUM = "abs(sum(ALL_PARAMS))";
	
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	private final UebertragTransactionFilter uebertragTransactionFilter;
	
	@Autowired
	public IncomeOutgoingPieChartCalculator(
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
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors,
			Entry<Pair<Long, String>, List<CashAccountTransaction>> entry,
			BigDecimal sumOfAllTransactions) {
		
		DtoChartSeries<Object, Number> result = new DtoChartSeries<>();
		
		Pair<Long, String> group = entry.getKey();
		String categoryName = group.getRight();
		
		result.setName(categoryName);
		result.getParams().put(CATEGORY_ID, entry.getKey().getLeft());
		result.getParams().put(CASHACCOUNT_IDS, cashAccountIds);
		result.getParams().put(START_DATE, startDate);
		result.getParams().put(END_DATE, endDate);
		
		result.setColor(StringColorUtil.convertTextToColor(categoryName));
		
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
		
		result.setPoints(Lists.newArrayList(new DtoChartPoint(categoryName, sum, description)));
		
		return result;
	}
	
	private BigDecimal calculateTransactionValueInEuro(
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors,
			CashAccountTransaction transaction) {
		
		return assetEuroValueCalculator.calculateEuroValue(
				transaction.getValue(),
				transaction.getValutaDate().atStartOfDay(),
				transaction.getCashAccount().getCurrency(),
				currencyConversionFactors);
	}
}
