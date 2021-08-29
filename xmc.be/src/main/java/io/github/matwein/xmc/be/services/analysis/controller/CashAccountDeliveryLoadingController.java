package io.github.matwein.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.be.common.LocalDateUtil;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.common.CommonConstants;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
	private final LocalDateUtil localDateUtil;
	
	@Autowired
	public CashAccountDeliveryLoadingController(
			CashAccountJpaRepository cashAccountJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			LocalDateUtil localDateUtil) {
		
		this.cashAccountJpaRepository = cashAccountJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.localDateUtil = localDateUtil;
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
		
		CashAccount cashAccount = cashAccountJpaRepository.getById(cashAccountId);
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
			long date = localDateUtil.toMillis(currentDate.atTime(CommonConstants.END_OF_DAY));
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
