package io.github.matwein.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import io.github.matwein.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import io.github.matwein.xmc.common.stubs.depot.items.DtoDepotItem;

@Component
public class DtoDepotDeliveryImportRowToDtoDepotItemMapper {
	public DtoDepotItem map(DtoDepotDeliveryImportRow dtoDepotDeliveryImportRow) {
		var dto = new DtoDepotItem();
		
		dto.setIsin(dtoDepotDeliveryImportRow.getIsin());
		dto.setAmount(dtoDepotDeliveryImportRow.getAmount());
		dto.setCourse(dtoDepotDeliveryImportRow.getCourse());
		dto.setValue(dtoDepotDeliveryImportRow.getValue());
		dto.setCurrency(dtoDepotDeliveryImportRow.getCurrency());
		
		return dto;
	}
}
