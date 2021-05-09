package io.github.matwein.xmc.be.services.analysis.mapper;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepotTransactionToDtoMostRecentTransactionMapper {
	public List<DtoMostRecentTransaction> mapAll(List<DepotTransaction> transactions) {
		return transactions.stream()
				.map(this::map)
				.collect(Collectors.toList());
	}
	
	private DtoMostRecentTransaction map(DepotTransaction transaction) {
		var mostRecentTransaction = new DtoMostRecentTransaction();
		
		Depot depot = transaction.getDepot();
		mostRecentTransaction.setAssetColor(depot.getColor());
		mostRecentTransaction.setAssetName(depot.getName());
		
		mostRecentTransaction.setCurrency(Currency.getInstance(transaction.getCurrency()));
		mostRecentTransaction.setDate(transaction.getValutaDate());
		mostRecentTransaction.setDescription(transaction.getDescription());
		mostRecentTransaction.setValue(transaction.getValue());
		mostRecentTransaction.setPositive(transaction.getValue().doubleValue() > 0.0);
		
		return mostRecentTransaction;
	}
}
