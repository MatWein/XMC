package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.PersistentObject;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.depot.CurrencyConversionFactor;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.services.ccf.controller.AssetEuroValueCalculator;
import org.xmc.be.services.ccf.controller.CurrencyConversionFactorLoadingController;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CashAccountDeliveryLoadingController {
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final AssetEuroValueCalculator assetEuroValueCalculator;
	private final CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController;
	
	@Autowired
	public CashAccountDeliveryLoadingController(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			AssetEuroValueCalculator assetEuroValueCalculator,
			CurrencyConversionFactorLoadingController currencyConversionFactorLoadingController) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.assetEuroValueCalculator = assetEuroValueCalculator;
		this.currencyConversionFactorLoadingController = currencyConversionFactorLoadingController;
	}
	
	public List<DtoAssetDeliveries> loadDeliveriesForCashAccounts(Collection<Long> cashAccountIds, LocalDate startDate, LocalDate endDate) {
		return cashAccountIds.stream()
				.map(cashAccountId -> loadDeliveriesForCashAccount(cashAccountId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetDeliveries loadDeliveriesForCashAccount(long cashAccountId, LocalDate startDate, LocalDate endDate) {
		CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
		Multimap<String, CurrencyConversionFactor> currencyConversionFactors = currencyConversionFactorLoadingController.load(cashAccount.getCurrency());
		
		DtoAssetDeliveries result = new DtoAssetDeliveries();
		
		result.setAssetId(cashAccountId);
		result.setAssetName(cashAccount.getName());
		result.setAssetType(AssetType.CASHACCOUNT);
		
		List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findAllTransactionsInRange(
				cashAccountId, startDate, endDate);
		
		List<Pair<LocalDateTime, Double>> mappedDeliveries = mapCashAccountDeliveries(transactions, currencyConversionFactors);
		
		List<CashAccountTransaction> transactionsBeforeOrOnDate = cashAccountTransactionJpaRepository.findTransactionsBeforeOrOnDate(
				cashAccount, startDate, LocalDateTime.now(), Long.MAX_VALUE, PageRequest.of(0, 1));
		
		if (transactionsBeforeOrOnDate.size() == 1) {
			double saldoBefore = transactionsBeforeOrOnDate.get(0).getSaldoAfter().doubleValue();
			mappedDeliveries.add(0, ImmutablePair.of(startDate.atStartOfDay(), saldoBefore));
			
			if (mappedDeliveries.size() == 0) {
				mappedDeliveries.add(ImmutablePair.of(endDate.atStartOfDay(), saldoBefore));
			}
		}
		
		result.setDeliveries(mappedDeliveries);
		
		return result;
	}
	
	private List<Pair<LocalDateTime, Double>> mapCashAccountDeliveries(
			List<CashAccountTransaction> transactions,
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		
		List<CashAccountTransaction> transactionsWithMaxDatePerDay = calculateTransactionsWithMaxDatePerDay(transactions);
		
		return transactionsWithMaxDatePerDay.stream()
				.flatMap(transaction -> mapCashAccountDelivery(transaction, currencyConversionFactors).stream())
				.collect(Collectors.toList());
	}
	
	private List<CashAccountTransaction> calculateTransactionsWithMaxDatePerDay(List<CashAccountTransaction> transactions) {
		Multimap<LocalDate, CashAccountTransaction> transactionsGroupedByDate = Multimaps.index(transactions, CashAccountTransaction::getValutaDate);
		List<CashAccountTransaction> result = Lists.newArrayList();
		
		for (Entry<LocalDate, Collection<CashAccountTransaction>> entry : transactionsGroupedByDate.asMap().entrySet()) {
			Optional<CashAccountTransaction> maxTransaction = entry.getValue().stream().max(Comparator.comparing(PersistentObject::getId));
			if (maxTransaction.isPresent()) {
				result.add(maxTransaction.get());
			}
		}
		
		return result;
	}
	
	private List<Pair<LocalDateTime, Double>> mapCashAccountDelivery(
			CashAccountTransaction transaction,
			Multimap<String, CurrencyConversionFactor> currencyConversionFactors) {
		
		List<Pair<LocalDateTime, Double>> points = Lists.newArrayList();
		
		LocalDate valutaDate = transaction.getValutaDate();
		
		BigDecimal euroValueBefore = assetEuroValueCalculator.calculateEuroValue(
				transaction.getSaldoBefore(),
				transaction.getValutaDate().atStartOfDay().minusSeconds(1),
				transaction.getCashAccount().getCurrency(),
				currencyConversionFactors);
		
		BigDecimal euroValueAfter = assetEuroValueCalculator.calculateEuroValue(
				transaction.getSaldoAfter(),
				transaction.getValutaDate().atStartOfDay(),
				transaction.getCashAccount().getCurrency(),
				currencyConversionFactors);
		
		points.add(ImmutablePair.of(valutaDate.atStartOfDay().minusSeconds(1), euroValueBefore.doubleValue()));
		points.add(ImmutablePair.of(valutaDate.atStartOfDay(), euroValueAfter.doubleValue()));
		
		return points;
	}
}
