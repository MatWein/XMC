package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DtoDepotDeliveryToDepotDeliveryMapper {
	public DepotDelivery map(Depot depot, DtoDepotDelivery dtoDepotDelivery) {
		var depotDelivery = new DepotDelivery();
		
		depotDelivery.setDepot(depot);
		
		update(depotDelivery, dtoDepotDelivery);
		
		return depotDelivery;
	}
	
	public void update(DepotDelivery depotDelivery, DtoDepotDelivery dtoDepotDelivery) {
		depotDelivery.setDeliveryDate(dtoDepotDelivery.getDeliveryDate());
		
		if (depotDelivery.getSaldo() == null) {
			depotDelivery.setSaldo(BigDecimal.valueOf(0.0));
		}
	}
}
