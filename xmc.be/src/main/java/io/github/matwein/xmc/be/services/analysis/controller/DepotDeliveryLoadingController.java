package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.be.common.LocalDateUtil;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.common.CommonConstants;
import io.github.matwein.xmc.common.stubs.analysis.AssetType;
import io.github.matwein.xmc.common.stubs.analysis.DtoAssetPoints;
import io.github.matwein.xmc.common.stubs.analysis.charts.DtoChartPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DepotDeliveryLoadingController {
	private final DepotJpaRepository depotJpaRepository;
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final LocalDateUtil localDateUtil;
	
	@Autowired
	public DepotDeliveryLoadingController(
			DepotJpaRepository depotJpaRepository,
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			LocalDateUtil localDateUtil) {
		
		this.depotJpaRepository = depotJpaRepository;
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.localDateUtil = localDateUtil;
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
		
		Depot depot = depotJpaRepository.getReferenceById(depotId);
		result.setAssetName(depot.getName());
		result.setAssetColor(depot.getColor());
		
		result.setPoints(loadDeliveryPoints(depot, startDate, endDate));
		
		return result;
	}
	
	private List<DtoChartPoint<Number, Number>> loadDeliveryPoints(Depot depot, LocalDate startDate, LocalDate endDate) {
		long days = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
		List<DtoChartPoint<Number, Number>> result = new ArrayList<>((int)days);
		
		List<DepotDelivery> deliveries = depotDeliveryJpaRepository.findByDepotAndDeletionDateIsNull(depot);
		
		for (LocalDate currentDate = startDate; currentDate.isBefore(endDate) || currentDate.isEqual(endDate); currentDate = currentDate.plusDays(1)) {
			LocalDateTime date = currentDate.atTime(CommonConstants.END_OF_DAY);
			Optional<DepotDelivery> delivery = findLastDeliveryBeforeOrOnDate(date, deliveries);
			double valueAtDate = delivery.map(DepotDelivery::getSaldo).orElse(BigDecimal.ZERO).doubleValue();
			
			result.add(new DtoChartPoint<>(localDateUtil.toMillis(date), valueAtDate));
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
