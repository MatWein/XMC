package io.github.matwein.xmc.be.services.depot.controller;

import io.github.matwein.xmc.be.entities.depot.DepotDelivery;
import io.github.matwein.xmc.be.entities.depot.DepotItem;
import io.github.matwein.xmc.be.repositories.depot.DepotItemJpaRepository;
import io.github.matwein.xmc.be.services.depot.mapper.DtoDepotItemToDepotItemMapper;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
		depotDelivery.getDepotItems().add(depotItemJpaRepository.save(depotItem));
	}
	
	private DepotItem createOrUpdateDepotItem(DtoDepotItem dtoDepotItem, DepotDelivery depotDelivery) {
		if (dtoDepotItem.getId() == null) {
			return dtoDepotItemToDepotItemMapper.map(depotDelivery, dtoDepotItem);
		} else {
			DepotItem depotItem = depotItemJpaRepository.getReferenceById(dtoDepotItem.getId());
			dtoDepotItemToDepotItemMapper.update(depotItem, dtoDepotItem);
			return depotItem;
		}
	}
}
