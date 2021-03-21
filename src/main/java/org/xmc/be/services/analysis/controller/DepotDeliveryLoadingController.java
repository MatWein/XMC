package org.xmc.be.services.analysis.controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.common.CommonConstants;
import org.xmc.common.stubs.analysis.DtoAssetDeliveries;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepotDeliveryLoadingController {
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	
	@Autowired
	public DepotDeliveryLoadingController(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotJpaRepository depotJpaRepository) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
	}
	
	public List<DtoAssetDeliveries> loadDeliveriesForDepots(Collection<Long> depotIds, LocalDate startDate, LocalDate endDate) {
		return depotIds.stream()
				.map(depotId -> loadDeliveriesForDepot(depotId, startDate, endDate))
				.collect(Collectors.toList());
	}
	
	private DtoAssetDeliveries loadDeliveriesForDepot(long depotId, LocalDate startDate, LocalDate endDate) {
		Depot depot = depotJpaRepository.getOne(depotId);
		
		List<DepotDelivery> deliveries = depotDeliveryJpaRepository.findAllDeliveriesInRange(
				depotId, startDate.atStartOfDay(), endDate.atTime(CommonConstants.END_OF_DAY));
		
		DtoAssetDeliveries result = new DtoAssetDeliveries();
		
		result.setAssetId(depotId);
		result.setAssetName(depot.getName());
		result.setDeliveries(mapDepotDeliveries(deliveries));
		
		return result;
	}
	
	private List<Pair<LocalDateTime, Double>> mapDepotDeliveries(List<DepotDelivery> deliveries) {
		return deliveries.stream()
				.map(this::mapDepotDelivery)
				.collect(Collectors.toList());
	}
	
	private Pair<LocalDateTime, Double> mapDepotDelivery(DepotDelivery depotDelivery) {
		return ImmutablePair.of(depotDelivery.getDeliveryDate(), depotDelivery.getSaldo().doubleValue());
	}
}
