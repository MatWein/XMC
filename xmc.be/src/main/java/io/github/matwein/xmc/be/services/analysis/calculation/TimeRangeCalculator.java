package io.github.matwein.xmc.be.services.analysis.calculation;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.TimeRange;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
	
	public Pair<LocalDate, LocalDate> calculateStartAndEndDate(TimeRange timeRange, Map<AssetType, List<Long>> assetIds) {
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
	
	private Optional<LocalDate> calculateForMaximumTimeRange(Map<AssetType, List<Long>> assetIds) {
		if (assetIds.isEmpty()) {
			return Optional.empty();
		}
		
		Optional<LocalDate> startDate = Optional.empty();
		
		for (Entry<AssetType, List<Long>> entry : assetIds.entrySet()) {
			Optional<LocalDate> minimumDateForAssets = loadMinimumDateForAssets(entry.getKey(), entry.getValue());
			
			if (minimumDateForAssets.isPresent() && (startDate.isEmpty() || startDate.get().isAfter(minimumDateForAssets.get()))) {
				startDate = minimumDateForAssets;
			}
		}
		
		return startDate.map(date -> date.minusDays(1));
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
