package org.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDelivery;

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
