package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.springframework.stereotype.Component;

@Component
public class DtoDepotTransactionToDepotTransactionMapper {
	public DepotTransaction map(Depot depot, DtoDepotTransaction dtoTransaction) {
		var depotTransaction = new DepotTransaction();
		
		depotTransaction.setDepot(depot);
		
		update(depotTransaction, dtoTransaction);
		
		return depotTransaction;
	}
	
	public void update(DepotTransaction depotTransaction, DtoDepotTransaction dtoTransaction) {
		depotTransaction.setAmount(dtoTransaction.getAmount());
		depotTransaction.setCourse(dtoTransaction.getCourse());
		depotTransaction.setCurrency(dtoTransaction.getCurrency());
		depotTransaction.setDescription(dtoTransaction.getDescription());
		depotTransaction.setIsin(dtoTransaction.getIsin());
		depotTransaction.setValue(dtoTransaction.getValue());
		depotTransaction.setValutaDate(dtoTransaction.getValutaDate());
	}
}
