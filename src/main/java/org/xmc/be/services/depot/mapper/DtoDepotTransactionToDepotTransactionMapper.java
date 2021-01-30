package org.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;

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
		depotTransaction.setCurrency(dtoTransaction.getCurrency().getCurrencyCode());
		depotTransaction.setDescription(dtoTransaction.getDescription());
		depotTransaction.setIsin(dtoTransaction.getIsin());
		depotTransaction.setValue(dtoTransaction.getValue());
		depotTransaction.setValutaDate(dtoTransaction.getValutaDate());
	}
}
