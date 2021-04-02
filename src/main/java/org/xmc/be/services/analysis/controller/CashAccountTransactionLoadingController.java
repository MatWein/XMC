package org.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.utils.LocalDateUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class CashAccountTransactionLoadingController {
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	
	@Autowired
	public CashAccountTransactionLoadingController(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
	}
	
	public List<DtoAssetPoints> loadTransactionsForCashAccounts(List<Long> cashAccountIds, LocalDate startDate, LocalDate endDate) {
		return cashAccountIds.stream()
				.map(cashAccountId -> loadDeliveriesForCashAccount(cashAccountId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetPoints loadDeliveriesForCashAccount(long cashAccountId, LocalDate startDate, LocalDate endDate) {
		DtoAssetPoints result = new DtoAssetPoints();
		
		result.setAssetId(cashAccountId);
		result.setAssetType(AssetType.CASHACCOUNT);
		
		CashAccount cashAccount = cashAccountJpaRepository.getOne(cashAccountId);
		result.setAssetName(cashAccount.getName());
		result.setAssetColor(cashAccount.getColor());
		
		result.setPoints(loadTransactionPoints(cashAccount, startDate, endDate));
		
		return result;
	}
	
	private List<DtoChartPoint<Number, Number>> loadTransactionPoints(CashAccount cashAccount, LocalDate startDate, LocalDate endDate) {
		List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount);
		
		AtomicInteger counter = new AtomicInteger(1);
		
		return transactions.stream()
				.filter(transaction -> transaction.getValutaDate().compareTo(startDate) >= 0)
				.filter(transaction -> transaction.getValutaDate().compareTo(endDate) <= 0)
				.map(transaction -> new DtoChartPoint<Number, Number>(
						LocalDateUtil.toMillis(transaction.getValutaDate().atTime(transaction.getCreationDate().toLocalTime()).plusSeconds(counter.getAndIncrement())),
						transaction.getValue().doubleValue(),
						transaction.getUsage()))
				.sorted(Comparator.comparing(point -> point.getX().doubleValue()))
				.collect(Collectors.toList());
	}
}
