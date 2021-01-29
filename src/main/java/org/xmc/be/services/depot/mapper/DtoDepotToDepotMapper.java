package org.xmc.be.services.depot.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.depot.Depot;
import org.xmc.common.stubs.depot.DtoDepot;

@Component
public class DtoDepotToDepotMapper {
	public Depot map(Bank bank, DtoDepot dtoDepot) {
		Depot depot = new Depot();
		update(depot, bank, dtoDepot);
		return depot;
	}
	
	public void update(Depot depot, Bank bank, DtoDepot dtoDepot) {
		depot.setBank(bank);
		depot.setName(dtoDepot.getName());
		depot.setNumber(dtoDepot.getNumber());
	}
}
