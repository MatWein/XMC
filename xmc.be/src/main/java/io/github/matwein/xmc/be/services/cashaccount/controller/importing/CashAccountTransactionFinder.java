package io.github.matwein.xmc.be.services.cashaccount.controller.importing;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CashAccountTransactionFinder {
	public Optional<CashAccountTransaction> findExistingTransaction(
			List<CashAccountTransaction> allTransactions,
			DtoCashAccountTransaction transactionToFind) {
		
		String businessKeyToFind = generateBusinessKey(transactionToFind);
		
		List<CashAccountTransaction> matches = allTransactions.stream()
				.filter(transaction -> generateBusinessKey(transaction).equals(businessKeyToFind))
				.collect(Collectors.toList());
		
		if (matches.size() == 1) {
			return Optional.of(matches.get(0));
		}
		return Optional.empty();
	}
	
	private String generateBusinessKey(CashAccountTransaction transaction) {
		return generateBusinessKey(transaction.getValutaDate(), transaction.getValue());
	}
	
	private String generateBusinessKey(DtoCashAccountTransaction transaction) {
		return generateBusinessKey(transaction.getValutaDate(), transaction.getValue());
	}
	
	private String generateBusinessKey(LocalDate valutaDate, BigDecimal value) {
		return String.join("|", valutaDate.toString(), String.valueOf(value.doubleValue()));
	}
}
