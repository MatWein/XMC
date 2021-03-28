package org.xmc.be.services.analysis.controller;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.common.CommonConstants;
import org.xmc.common.stubs.analysis.AssetType;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepotDeliveryLoadingController {
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	
	@Autowired
	public DepotDeliveryLoadingController(
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
	}
	
	public List<DtoAssetDeliveries> loadDeliveriesForDepots(List<Long> depotIds, LocalDate startDate, LocalDate endDate) {
		return depotIds.stream()
				.map(cashAccountId -> loadDeliveriesForDepot(cashAccountId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetDeliveries loadDeliveriesForDepot(long depotId, LocalDate startDate, LocalDate endDate) {
		DtoAssetDeliveries result = new DtoAssetDeliveries();
		
		result.setAssetId(depotId);
		result.setAssetType(AssetType.DEPOT);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		result.setAssetName(depot.getName());
		
		result.setDeliveries(loadDeliveryPoints(depot, startDate, endDate));
		
		return result;
	}
	
	private List<Pair<LocalDateTime, Double>> loadDeliveryPoints(Depot depot, LocalDate startDate, LocalDate endDate) {
		long days = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
		List<Pair<LocalDateTime, Double>> result = Lists.newArrayListWithExpectedSize((int)days);
		
		List<DepotDelivery> deliveries = depotDeliveryJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		for (LocalDate currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
			LocalDateTime date = currentDate.atTime(CommonConstants.END_OF_DAY);
			Optional<DepotDelivery> delivery = findLastDeliveryBeforeOrOnDate(date, deliveries);
			double valueAtDate = delivery.map(DepotDelivery::getSaldo).orElse(BigDecimal.ZERO).doubleValue();
			
			result.add(ImmutablePair.of(date, valueAtDate));
		}
		
		return result;
	}
	
	private Optional<DepotDelivery> findLastDeliveryBeforeOrOnDate(LocalDateTime date, List<DepotDelivery> deliveries) {
		return deliveries.stream()
				.filter(delivery -> delivery.getDeliveryDate().isBefore(date) || delivery.getDeliveryDate().isEqual(date))
				.max(Comparator.comparing(DepotDelivery::getDeliveryDate)
						.thenComparing(DepotDelivery::getCreationDate)
						.thenComparing(DepotDelivery::getId));
	}
}
