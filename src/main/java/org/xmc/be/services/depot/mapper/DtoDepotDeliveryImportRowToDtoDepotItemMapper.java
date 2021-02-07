package org.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import org.xmc.common.stubs.depot.deliveries.DtoDepotDeliveryImportRow;
import org.xmc.common.stubs.depot.deliveries.DtoDepotItem;

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
