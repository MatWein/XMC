package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import org.springframework.stereotype.Component;

@Component
public class DtoDepotItemToDepotItemMapper {
	public DepotItem map(DepotDelivery depotDelivery, DtoDepotItem dtoDepotItem) {
		var depotItem = new DepotItem();
		
		depotItem.setDelivery(depotDelivery);
		
		update(depotItem, dtoDepotItem);
		
		return depotItem;
	}
	
	public void update(DepotItem depotItem, DtoDepotItem dtoDepotItem) {
		depotItem.setAmount(dtoDepotItem.getAmount());
		depotItem.setCourse(dtoDepotItem.getCourse());
		depotItem.setValue(dtoDepotItem.getValue());
		depotItem.setIsin(dtoDepotItem.getIsin());
		depotItem.setCurrency(dtoDepotItem.getCurrency());
	}
}
