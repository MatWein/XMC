package org.xmc.be.services.analysis.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.xmc.common.utils.StringColorUtil;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CashAccountTransactionToDtoMostRecentTransactionMapper {
	public List<DtoMostRecentTransaction> mapAll(List<CashAccountTransaction> transactions) {
		return transactions.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private DtoMostRecentTransaction map(CashAccountTransaction transaction) {
		var mostRecentTransaction = new DtoMostRecentTransaction();
		
		CashAccount cashAccount = transaction.getCashAccount();
		mostRecentTransaction.setAssetColor(StringColorUtil.convertStringToAwtColor(cashAccount.getColor()));
		mostRecentTransaction.setAssetName(cashAccount.getName());
		mostRecentTransaction.setCurrency(cashAccount.getCurrency());
		
		mostRecentTransaction.setDate(transaction.getValutaDate());
		mostRecentTransaction.setDescription(transaction.getUsage());
		mostRecentTransaction.setValue(transaction.getValue());
		mostRecentTransaction.setPositive(transaction.getValue().doubleValue() > 0.0);
		
		return mostRecentTransaction;
	}
}
