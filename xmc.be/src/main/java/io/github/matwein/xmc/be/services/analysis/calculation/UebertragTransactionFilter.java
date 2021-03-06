package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UebertragTransactionFilter {
	public Predicate<CashAccountTransaction> createNonUebertragPredicate(List<CashAccountTransaction> allTransactions) {
		return transaction -> {
			LocalDate startDate = transaction.getValutaDate().minusDays(5);
			LocalDate endDate = transaction.getValutaDate().plusDays(5);
			
			return allTransactions.stream()
					.filter(t -> t.getValue().doubleValue() == transaction.getValue().multiply(BigDecimal.valueOf(-1.0)).doubleValue())
					.filter(t -> t.getValutaDate().compareTo(startDate) >= 0)
					.filter(t -> t.getValutaDate().compareTo(endDate) <= 0)
					.filter(t -> !t.getCashAccount().equals(transaction.getCashAccount()))
					.count() == 0;
		};
	}
}
