package org.xmc.be.services.analysis.controller;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;
import org.xmc.common.CommonConstants;
import org.xmc.common.stubs.analysis.AssetType;
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
		
		DtoAssetDeliveries result = new DtoAssetDeliveries();
		
		result.setAssetId(depotId);
		result.setAssetName(depot.getName());
		result.setAssetType(AssetType.DEPOT);
		
		List<DepotDelivery> deliveries = depotDeliveryJpaRepository.findAllDeliveriesInRange(
				depotId, startDate.atStartOfDay(), endDate.atTime(CommonConstants.END_OF_DAY));
		
		List<Pair<LocalDateTime, Double>> mappedDeliveries = mapDepotDeliveries(deliveries);
		
		List<DepotDelivery> deliveriesBeforeOrOnDate = depotDeliveryJpaRepository.findDeliveryBeforeOrOnDate(
				depot, startDate.atTime(CommonConstants.END_OF_DAY), PageRequest.of(0, 1));
		
		if (deliveriesBeforeOrOnDate.size() == 1) {
			double saldoBefore = deliveriesBeforeOrOnDate.get(0).getSaldo().doubleValue();
			mappedDeliveries.add(0, ImmutablePair.of(startDate.atStartOfDay(), saldoBefore));
			
			if (mappedDeliveries.size() == 0) {
				mappedDeliveries.add(ImmutablePair.of(endDate.atStartOfDay(), saldoBefore));
			}
		}
		
		result.setDeliveries(mappedDeliveries);
		
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
