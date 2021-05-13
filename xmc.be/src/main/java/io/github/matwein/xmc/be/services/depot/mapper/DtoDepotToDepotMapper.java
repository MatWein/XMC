package io.github.matwein.xmc.be.services.depot.mapper;

import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;
import org.springframework.stereotype.Component;

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
		depot.setColor(dtoDepot.getColor());
	}
}
