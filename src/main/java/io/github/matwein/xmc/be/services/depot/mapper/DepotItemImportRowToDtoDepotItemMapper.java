package io.github.matwein.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItemImportRow;

@Component
public class DepotItemImportRowToDtoDepotItemMapper {
	public DtoDepotItem map(DtoDepotItemImportRow depotItemImportRow) {
		var dto = new DtoDepotItem();
		
		dto.setAmount(depotItemImportRow.getAmount());
		dto.setCourse(depotItemImportRow.getCourse());
		dto.setCurrency(depotItemImportRow.getCurrency());
		dto.setIsin(depotItemImportRow.getIsin());
		dto.setValue(depotItemImportRow.getValue());
		
		return dto;
	}
}
