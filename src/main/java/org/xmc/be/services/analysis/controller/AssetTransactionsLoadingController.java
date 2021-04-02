package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetPoints;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class AssetTransactionsLoadingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetTransactionsLoadingController.class);
	
	private final CashAccountTransactionLoadingController cashAccountTransactionLoadingController;
	private final DepotTransactionLoadingController depotTransactionLoadingController;
	
	@Autowired
	public AssetTransactionsLoadingController(
			CashAccountTransactionLoadingController cashAccountTransactionLoadingController,
			DepotTransactionLoadingController depotTransactionLoadingController) {
		
		this.cashAccountTransactionLoadingController = cashAccountTransactionLoadingController;
		this.depotTransactionLoadingController = depotTransactionLoadingController;
	}
	
	public List<DtoAssetPoints> loadAssetDeliveries(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetPoints> deliveries = Lists.newArrayList();
		
		for (Entry<AssetType, Collection<Long>> entry : assetIds.asMap().entrySet()) {
			List<DtoAssetPoints> transactionsForAssetType = loadTransactionsForAssetType(entry.getKey(), entry.getValue(), startDate, endDate);
			deliveries.addAll(transactionsForAssetType);
		}
		
		return deliveries;
	}
	
	private List<DtoAssetPoints> loadTransactionsForAssetType(
			AssetType assetType,
			Collection<Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		List<Long> sortedAssetIds = assetIds.stream().sorted().collect(Collectors.toList());
		
		if (assetType == AssetType.CASHACCOUNT) {
			return cashAccountTransactionLoadingController.loadTransactionsForCashAccounts(sortedAssetIds, startDate, endDate);
		} else if (assetType == AssetType.DEPOT) {
			return depotTransactionLoadingController.loadTransactionsForDepots(sortedAssetIds, startDate, endDate);
		} else {
			String message = String.format("Could not load deliveries for asset of unknown type '%s'.", assetType);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
