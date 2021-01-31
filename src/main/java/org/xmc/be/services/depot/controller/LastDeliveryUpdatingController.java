package org.xmc.be.services.depot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.repositories.depot.DepotDeliveryJpaRepository;
import org.xmc.be.repositories.depot.DepotDeliveryRepository;
import org.xmc.be.repositories.depot.DepotJpaRepository;

import java.util.Optional;

@Component
public class LastDeliveryUpdatingController {
	private final DepotDeliveryJpaRepository depotDeliveryJpaRepository;
	private final DepotDeliveryRepository depotDeliveryRepository;
	private final DepotJpaRepository depotJpaRepository;
	
	@Autowired
	public LastDeliveryUpdatingController(
			DepotDeliveryJpaRepository depotDeliveryJpaRepository,
			DepotDeliveryRepository depotDeliveryRepository,
			DepotJpaRepository depotJpaRepository) {
		
		this.depotDeliveryJpaRepository = depotDeliveryJpaRepository;
		this.depotDeliveryRepository = depotDeliveryRepository;
		this.depotJpaRepository = depotJpaRepository;
	}
	
	public void updateLastDeliveryOfDepot(Depot depot) {
		depotDeliveryJpaRepository.flush();
		
		Optional<DepotDelivery> lastDelivery = depotDeliveryRepository.loadMostRecentDeliveryOfDepot(depot);
		if (lastDelivery.isPresent()) {
			depot.setLastDelivery(lastDelivery.get());
		} else {
			depot.setLastDelivery(null);
		}
		
		depotJpaRepository.save(depot);
	}
}
