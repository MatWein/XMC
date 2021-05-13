package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransactionImportRow;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionImportRowToDtoDepotTransactionMapper {
	public DtoDepotTransaction map(DtoDepotTransactionImportRow depotTransactionImportRow) {
		var dto = new DtoDepotTransaction();
		
		dto.setValutaDate(depotTransactionImportRow.getValutaDate());
		dto.setAmount(depotTransactionImportRow.getAmount());
		dto.setCourse(depotTransactionImportRow.getCourse());
		dto.setCurrency(depotTransactionImportRow.getCurrency());
		dto.setIsin(depotTransactionImportRow.getIsin());
		dto.setValue(depotTransactionImportRow.getValue());
		dto.setDescription(depotTransactionImportRow.getDescription());
		
		return dto;
	}
}
