package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class AssetDeliveriesLoadingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AssetDeliveriesLoadingController.class);
	
	private final CashAccountDeliveryLoadingController cashAccountDeliveryLoadingController;
	
	@Autowired
	public AssetDeliveriesLoadingController(CashAccountDeliveryLoadingController cashAccountDeliveryLoadingController) {
		this.cashAccountDeliveryLoadingController = cashAccountDeliveryLoadingController;
	}
	
	public List<DtoAssetDeliveries> loadAssetDeliveries(Multimap<AssetType, Long> assetIds, LocalDate startDate, LocalDate endDate) {
		List<DtoAssetDeliveries> deliveries = Lists.newArrayList();
		
		for (Entry<AssetType, Collection<Long>> entry : assetIds.asMap().entrySet()) {
			List<DtoAssetDeliveries> deliveriesForAssetType = loadDeliveriesForAssetType(entry.getKey(), entry.getValue(), startDate, endDate);
			deliveries.addAll(deliveriesForAssetType);
		}
		
		return deliveries;
	}
	
	private List<DtoAssetDeliveries> loadDeliveriesForAssetType(
			AssetType assetType,
			Collection<Long> assetIds,
			LocalDate startDate,
			LocalDate endDate) {
		
		List<Long> sortedAssetIds = assetIds.stream().sorted().collect(Collectors.toList());
		
		if (assetType == AssetType.CASHACCOUNT) {
			return cashAccountDeliveryLoadingController.loadDeliveriesForCashAccounts(sortedAssetIds, startDate, endDate);
		} else if (assetType == AssetType.DEPOT) {
			return Lists.newArrayList();
		} else {
			String message = String.format("Could not load deliveries for asset of unknown type '%s'.", assetType);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
