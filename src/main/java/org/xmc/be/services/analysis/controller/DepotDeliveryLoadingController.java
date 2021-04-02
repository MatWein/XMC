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
import org.xmc.common.stubs.analysis.DtoAssetPoints;
import org.xmc.common.utils.LocalDateUtil;

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
	
	public List<DtoAssetPoints> loadDeliveriesForDepots(List<Long> depotIds, LocalDate startDate, LocalDate endDate) {
		return depotIds.stream()
				.map(cashAccountId -> loadDeliveriesForDepot(cashAccountId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetPoints loadDeliveriesForDepot(long depotId, LocalDate startDate, LocalDate endDate) {
		DtoAssetPoints result = new DtoAssetPoints();
		
		result.setAssetId(depotId);
		result.setAssetType(AssetType.DEPOT);
		
		Depot depot = depotJpaRepository.getOne(depotId);
		result.setAssetName(depot.getName());
		result.setAssetColor(depot.getColor());
		
		result.setPoints(loadDeliveryPoints(depot, startDate, endDate));
		
		return result;
	}
	
	private List<Pair<Number, Number>> loadDeliveryPoints(Depot depot, LocalDate startDate, LocalDate endDate) {
		long days = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
		List<Pair<Number, Number>> result = Lists.newArrayListWithExpectedSize((int)days);
		
		List<DepotDelivery> deliveries = depotDeliveryJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		for (LocalDate currentDate = startDate; currentDate.isBefore(endDate) || currentDate.isEqual(endDate); currentDate = currentDate.plusDays(1)) {
			LocalDateTime date = currentDate.atTime(CommonConstants.END_OF_DAY);
			Optional<DepotDelivery> delivery = findLastDeliveryBeforeOrOnDate(date, deliveries);
			double valueAtDate = delivery.map(DepotDelivery::getSaldo).orElse(BigDecimal.ZERO).doubleValue();
			
			result.add(ImmutablePair.of(LocalDateUtil.toMillis(date), valueAtDate));
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
