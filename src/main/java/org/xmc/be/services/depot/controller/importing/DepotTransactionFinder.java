package org.xmc.be.services.depot.controller.importing;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;

import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepotTransactionFinder {
	public Optional<DepotTransaction> findExistingTransaction(
			List<DepotTransaction> existingDepotTransactions,
			DtoDepotTransactionImportRow row) {
		
		List<DepotTransaction> matches = existingDepotTransactions.stream()
				.filter(transaction -> transaction.getIsin().equalsIgnoreCase(row.getIsin()))
				.filter(transaction -> transaction.getValutaDate().equals(row.getValutaDate()))
				.filter(transaction -> transaction.getAmount().setScale(2, RoundingMode.HALF_UP).equals(row.getAmount().setScale(2, RoundingMode.HALF_UP)))
				.filter(transaction -> transaction.getCurrency().equals(row.getCurrency()))
				.collect(Collectors.toList());
		
		if (matches.size() == 1) {
			return Optional.of(matches.get(0));
		}
		return Optional.empty();
	}
}
