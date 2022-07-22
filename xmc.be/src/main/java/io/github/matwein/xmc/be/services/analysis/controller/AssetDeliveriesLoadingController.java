package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class AssetDeliveriesLoadingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetDeliveriesLoadingController.class);
	
	private final CashAccountDeliveryLoadingController cashAccountDeliveryLoadingController;
	private final DepotDeliveryLoadingController depotDeliveryLoadingController;
	
	@Autowired
	public AssetDeliveriesLoadingController(
			CashAccountDeliveryLoadingController cashAccountDeliveryLoadingController,
			DepotDeliveryLoadingController depotDeliveryLoadingController) {
		
		this.cashAccountDeliveryLoadingController = cashAccountDeliveryLoadingController;
		this.depotDeliveryLoadingController = depotDeliveryLoadingController;
	}
	
	public List<DtoAssetPoints> loadAssetDeliveries(Map<AssetType, List<Long>> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetPoints> deliveries = new ArrayList<>();
		
		for (Entry<AssetType, List<Long>> entry : assetIds.entrySet()) {
			List<DtoAssetPoints> deliveriesForAssetType = loadDeliveriesForAssetType(entry.getKey(), entry.getValue(), startDate, endDate);
			deliveries.addAll(deliveriesForAssetType);
		}
		
		return deliveries;
	}
	
	private List<DtoAssetPoints> loadDeliveriesForAssetType(
			AssetType assetType,
			Collection<Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		List<Long> sortedAssetIds = assetIds.stream().sorted().collect(Collectors.toList());
		
		if (assetType == AssetType.CASHACCOUNT) {
			return cashAccountDeliveryLoadingController.loadDeliveriesForCashAccounts(sortedAssetIds, startDate, endDate);
		} else if (assetType == AssetType.DEPOT) {
			return depotDeliveryLoadingController.loadDeliveriesForDepots(sortedAssetIds, startDate, endDate);
		} else {
			String message = String.format("Could not load deliveries for asset of unknown type '%s'.", assetType);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
