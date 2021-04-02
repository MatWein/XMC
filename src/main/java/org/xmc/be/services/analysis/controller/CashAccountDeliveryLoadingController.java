package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.common.CommonConstants;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.xmc.common.utils.LocalDateUtil;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CashAccountDeliveryLoadingController {
	private final CashAccountJpaRepository cashAccountJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	
	@Autowired
	public CashAccountDeliveryLoadingController(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
	}
	
	public List<DtoAssetPoints> loadDeliveriesForCashAccounts(List<Long> cashAccountIds, LocalDate startDate, LocalDate endDate) {
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
		
		result.setPoints(loadDeliveryPoints(cashAccount, startDate, endDate));
		
		return result;
	}
	
	private List<DtoChartPoint<Number, Number>> loadDeliveryPoints(CashAccount cashAccount, LocalDate startDate, LocalDate endDate) {
		long days = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
		List<DtoChartPoint<Number, Number>> result = Lists.newArrayListWithExpectedSize((int)days);
		
		List<CashAccountTransaction> transactions = cashAccountTransactionJpaRepository.findByCashAccountAndDeletionDateIsNull(cashAccount);
		
		for (LocalDate currentDate = startDate; currentDate.isBefore(endDate) || currentDate.isEqual(endDate); currentDate = currentDate.plusDays(1)) {
			long date = LocalDateUtil.toMillis(currentDate.atTime(CommonConstants.END_OF_DAY));
			Optional<CashAccountTransaction> transaction = findLastTransactionBeforeOrOnDate(currentDate, transactions);
			double valueAtDate = transaction.map(CashAccountTransaction::getSaldoAfter).orElse(BigDecimal.ZERO).doubleValue();
			
			result.add(new DtoChartPoint(date, valueAtDate));
		}
		
		return result;
	}
	
	private Optional<CashAccountTransaction> findLastTransactionBeforeOrOnDate(LocalDate date, List<CashAccountTransaction> transactions) {
		return transactions.stream()
				.filter(transaction -> transaction.getValutaDate().isBefore(date) || transaction.getValutaDate().isEqual(date))
				.max(Comparator.comparing(CashAccountTransaction::getValutaDate)
						.thenComparing(CashAccountTransaction::getCreationDate)
						.thenComparing(CashAccountTransaction::getId));
	}
}
