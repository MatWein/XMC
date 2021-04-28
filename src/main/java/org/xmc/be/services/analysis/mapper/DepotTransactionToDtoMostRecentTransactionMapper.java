package org.xmc.be.services.analysis.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.xmc.common.utils.StringColorUtil;

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
		mostRecentTransaction.setAssetColor(StringColorUtil.convertStringToAwtColor(depot.getColor()));
		mostRecentTransaction.setAssetName(depot.getName());
		
		mostRecentTransaction.setCurrency(Currency.getInstance(transaction.getCurrency()));
		mostRecentTransaction.setDate(transaction.getValutaDate());
		mostRecentTransaction.setDescription(transaction.getDescription());
		mostRecentTransaction.setValue(transaction.getValue());
		mostRecentTransaction.setPositive(transaction.getValue().doubleValue() > 0.0);
		
		return mostRecentTransaction;
	}
}
