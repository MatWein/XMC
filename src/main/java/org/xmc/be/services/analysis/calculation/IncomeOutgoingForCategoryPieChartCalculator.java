package org.xmc.be.services.analysis.calculation;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Collection;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class IncomeOutgoingForCategoryPieChartCalculator {
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	
	@Autowired
	public IncomeOutgoingForCategoryPieChartCalculator(
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
		
		List<CashAccountTransaction> transactions = cashAccounts
				.stream()
				.flatMap(cashAccount -> cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount).stream())
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.filter(transaction -> {
					if (transaction.getCategory() == null) {
						return categoryId == null;
					} else {
						return transaction.getCategory().getId().equals(categoryId);
					}
				})
				.filter(transactionPredicate)
				.collect(Collectors.toList());
		
		BigDecimal sumOfAllTransactions = SCalcBuilder.bigDecimalInstance()
				.sumExpression()
				.build()
				.paramsAsCollection(transaction -> calculateTransactionValueInEuro(currencyConversionFactors, transaction), transactions)
				.calc();
		
		return transactions.stream()
				.map(transaction -> mapEntry(currencyConversionFactors, transaction, sumOfAllTransactions))
				.collect(Collectors.toList());
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
		
		BigDecimal percentage = SCalcBuilder.bigDecimalInstance()
				.expression("value * 100 / sumOfAllTransactions")
				.build()
				.parameter("value", value)
				.parameter("sumOfAllTransactions", sumOfAllTransactions)
				.calc();
		
		String description = String.format(
				"%s: %s\n%s: %s %%\n%s: %s",
				MessageAdapter.getByKey(MessageKey.ANALYSIS_SUM_IN_EUR),
				MessageAdapter.formatNumber(value),
				MessageAdapter.getByKey(MessageKey.ANALYSIS_PERCENTAGE),
				MessageAdapter.formatNumber(percentage),
				MessageAdapter.getByKey(MessageKey.ANALYSIS_DESCRIPTION),
				transaction.getUsage()
		);
		
		result.setPoints(Lists.newArrayList(new DtoChartPoint(name, value, description)));
		
		return result;
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
}
