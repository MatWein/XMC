package org.xmc.be.services.depot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.services.depot.mapper.DtoDepotDeliveryToDepotDeliveryMapper;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;

@Component
public class DepotDeliverySaveController {
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DtoDepotDeliveryToDepotDeliveryMapper dtoDepotDeliveryToDepotDeliveryMapper;
	
	@Autowired
	public DepotDeliverySaveController(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DtoDepotDeliveryToDepotDeliveryMapper dtoDepotDeliveryToDepotDeliveryMapper) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.dtoDepotDeliveryToDepotDeliveryMapper = dtoDepotDeliveryToDepotDeliveryMapper;
	}
	
	public DepotDelivery saveOrUpdate(Depot depot, DtoDepotDelivery dtoDepotDelivery) {
		DepotDelivery depotDelivery = createOrUpdateDepotDelivery(dtoDepotDelivery, depot);
		return depotDeliveryJpaRepository.save(depotDelivery);
	}
	
	private DepotDelivery createOrUpdateDepotDelivery(DtoDepotDelivery dtoDepotDelivery, Depot depot) {
		if (dtoDepotDelivery.getId() == null) {
			return dtoDepotDeliveryToDepotDeliveryMapper.map(depot, dtoDepotDelivery);
		} else {
			DepotDelivery depotDelivery = depotDeliveryJpaRepository.getOne(dtoDepotDelivery.getId());
			dtoDepotDeliveryToDepotDeliveryMapper.update(depotDelivery, dtoDepotDelivery);
			return depotDelivery;
		}
	}
}
