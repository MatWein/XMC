package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.services.ccf.controller.AssetEuroValueCalculator;
import org.xmc.be.services.ccf.controller.CurrencyConversionFactorLoadingController;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.stubs.analysis.charts.DtoChartSeries;
import org.xmc.common.utils.StringColorUtil;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import scalc.SCalcBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IncomeOutgoingPieChartCalculator {
	private static final String ABS_SUM = "abs(sum(ALL_PARAMS))";
	
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	
	@Autowired
	public IncomeOutgoingPieChartCalculator(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController,
			AssetEuroValueCalculator assetEuroValueCalculator) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.currencyConversionFactorLoadingController = currencyConversionFactorLoadingController;
		this.assetEuroValueCalculator = assetEuroValueCalculator;
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
		
		List<CashAccountTransaction> transactions = cashAccounts
				.stream()
				.flatMap(cashAccount -> cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount).stream())
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.filter(transactionPredicate)
				.collect(Collectors.toList());
		
		Map<String, List<CashAccountTransaction>> transactionsPerCategory = transactions.stream()
				.collect(Collectors.groupingBy(this::extractCategoryName));
		
		BigDecimal sumOfAllTransactions = SCalcBuilder.bigDecimalInstance()
				.expression(ABS_SUM)
				.build()
				.paramsAsCollection(transaction -> calculateTransactionValueInEuro(currencyConversionFactors, transaction), transactions)
				.calc();
		
		return transactionsPerCategory.entrySet().stream()
				.map(entry -> mapEntry(currencyConversionFactors, entry, sumOfAllTransactions))
				.collect(Collectors.toList());
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
	
	private String extractCategoryName(CashAccountTransaction transaction) {
		if (transaction.getCategory() == null) {
			return MessageAdapter.getByKey(MessageKey.ANALYSIS_OTHER);
		} else {
			return transaction.getCategory().getName();
		}
	}
	
	private DtoChartSeries<Object, Number> mapEntry(
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors,
			Entry<String, List<CashAccountTransaction>> entry,
			BigDecimal sumOfAllTransactions) {
		
		DtoChartSeries<Object, Number> result = new DtoChartSeries<>();
		
		String categoryName = entry.getKey();
		result.setName(categoryName);
		
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
}
