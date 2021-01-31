package org.xmc.be.services.depot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.DepotDelivery;
import org.xmc.be.entities.depot.DepotItem;
import org.xmc.be.repositories.depot.DepotItemJpaRepository;
import org.xmc.be.services.depot.mapper.DtoDepotItemToDepotItemMapper;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItem;

@Component
public class DepotItemSaveController {
	private final DepotItemJpaRepository depotItemJpaRepository;
	private final DtoDepotItemToDepotItemMapper dtoDepotItemToDepotItemMapper;
	
	@Autowired
	public DepotItemSaveController(
			DepotItemJpaRepository depotItemJpaRepository,
			DtoDepotItemToDepotItemMapper dtoDepotItemToDepotItemMapper) {
		
		this.depotItemJpaRepository = depotItemJpaRepository;
		this.dtoDepotItemToDepotItemMapper = dtoDepotItemToDepotItemMapper;
	}
	
	public void saveOrUpdate(DepotDelivery depotDelivery, DtoDepotItem dtoDepotItem) {
		DepotItem depotItem = createOrUpdateDepotItem(dtoDepotItem, depotDelivery);
		depotItemJpaRepository.save(depotItem);
	}
	
	private DepotItem createOrUpdateDepotItem(DtoDepotItem dtoDepotItem, DepotDelivery depotDelivery) {
		if (dtoDepotItem.getId() == null) {
			return dtoDepotItemToDepotItemMapper.map(depotDelivery, dtoDepotItem);
		} else {
			DepotItem depotItem = depotItemJpaRepository.getOne(dtoDepotItem.getId());
			dtoDepotItemToDepotItemMapper.update(depotItem, dtoDepotItem);
			return depotItem;
		}
	}
}
