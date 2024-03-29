package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.services.analysis.controller.CashAccountMostRecentTransactionLoadingController;
import io.github.matwein.xmc.be.services.analysis.controller.DepotMostRecentTransactionLoadingController;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class MostRecentTransactionsCalculator {
	private static final Logger LOGGER = LoggerFactory.getLogger(MostRecentTransactionsCalculator.class);
	
	public static final int MAX_TRANSACTIONS = 10;
	
	private final CashAccountMostRecentTransactionLoadingController cashAccountMostRecentTransactionLoadingController;
	private final DepotMostRecentTransactionLoadingController depotMostRecentTransactionLoadingController;
	
	@Autowired
	public MostRecentTransactionsCalculator(
			CashAccountMostRecentTransactionLoadingController cashAccountMostRecentTransactionLoadingController,
			DepotMostRecentTransactionLoadingController depotMostRecentTransactionLoadingController) {
		
		this.cashAccountMostRecentTransactionLoadingController = cashAccountMostRecentTransactionLoadingController;
		this.depotMostRecentTransactionLoadingController = depotMostRecentTransactionLoadingController;
	}
	
	public List<DtoMostRecentTransaction> calculate(Map<AssetType, List<Long>> assetIds) {
		List<DtoMostRecentTransaction> transactions = new ArrayList<>();
		
		for (Entry<AssetType, List<Long>> entry : assetIds.entrySet()) {
			List<DtoMostRecentTransaction> assetTransactions = loadTransactionsForAssetType(entry.getKey(), entry.getValue());
			transactions.addAll(assetTransactions);
		}
		
		return transactions
				.stream().sorted(Comparator.comparing(DtoMostRecentTransaction::getDate).reversed())
				.limit(MAX_TRANSACTIONS)
				.collect(Collectors.toList());
	}
	
	private List<DtoMostRecentTransaction> loadTransactionsForAssetType(
			AssetType assetType,
			Collection<Long> assetIds) {
		
		List<Long> sortedAssetIds = assetIds.stream().sorted().collect(Collectors.toList());
		
		if (assetType == AssetType.CASHACCOUNT) {
			return cashAccountMostRecentTransactionLoadingController.loadTransactionsForCashAccounts(sortedAssetIds);
		} else if (assetType == AssetType.DEPOT) {
			return depotMostRecentTransactionLoadingController.loadTransactionsForDepots(sortedAssetIds);
		} else {
			String message = String.format("Could not load transactions for assets of unknown type '%s'.", assetType);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
