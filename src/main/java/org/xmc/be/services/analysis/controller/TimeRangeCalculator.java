package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Multimap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.TimeRange;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Optional;

@Component
public class TimeRangeCalculator {
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeRangeCalculator.class);
	
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	
	@Autowired
	public TimeRangeCalculator(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
	}
	
	public Pair<LocalDate, LocalDate> calculateStartAndEndDate(TimeRange timeRange, Multimap<AssetType, Long> assetIds) {
		LocalDate startDate;
		LocalDate endDate = LocalDate.now();
		
		if (timeRange == TimeRange.LAST_MONTH) {
			startDate = LocalDate.now().minusMonths(1);
		} else if (timeRange == TimeRange.LAST_QUARTER) {
			startDate = LocalDate.now().minusMonths(3);
		} else if (timeRange == TimeRange.LAST_HALF_YEAR) {
			startDate = LocalDate.now().minusMonths(6);
		} else if (timeRange == TimeRange.LAST_YEAR) {
			startDate = LocalDate.now().minusMonths(12);
		} else if (timeRange == TimeRange.MAXIMUM) {
			startDate = calculateForMaximumTimeRange(assetIds).orElse(LocalDate.now().minusMonths(60));
		} else {
			String message = String.format("Could not calculate start/end date for time range '%s'.", timeRange);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
		
		return ImmutablePair.of(startDate, endDate);
	}
	
	private Optional<LocalDate> calculateForMaximumTimeRange(Multimap<AssetType, Long> assetIds) {
		if (assetIds.isEmpty()) {
			return Optional.empty();
		}
		
		Optional<LocalDate> startDate = Optional.empty();
		
		for (Entry<AssetType, Collection<Long>> entry : assetIds.asMap().entrySet()) {
			Optional<LocalDate> minimumDateForAssets = loadMinimumDateForAssets(entry.getKey(), entry.getValue());
			
			if (minimumDateForAssets.isPresent() && (startDate.isEmpty() || startDate.get().isAfter(minimumDateForAssets.get()))) {
				startDate = minimumDateForAssets;
			}
		}
		
		return startDate;
	}
	
	private Optional<LocalDate> loadMinimumDateForAssets(AssetType assetType, Collection<Long> assetids) {
		if (assetType == AssetType.CASHACCOUNT) {
			return cashAccountTransactionJpaRepository.findFirstTransaction(assetids).map(CashAccountTransaction::getValutaDate);
		} else if (assetType == AssetType.DEPOT) {
			return depotDeliveryJpaRepository.findFirstDelivery(assetids).map(DepotDelivery::getDeliveryDate).map(LocalDateTime::toLocalDate);
		} else {
			String message = String.format("Could not load minimum date for asset of unknown type '%s'.", assetType);
			LOGGER.error(message);
			throw new IllegalArgumentException(message);
		}
	}
}
