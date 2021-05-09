package io.github.matwein.xmc.be.services.depot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.repositories.depot.DepotJpaRepository;
import io.github.matwein.xmc.be.services.depot.mapper.DtoDepotToDepotMapper;
import io.github.matwein.xmc.common.stubs.depot.DtoDepot;

@Component
public class DepotSaveController {
	private final BankJpaRepository bankJpaRepository;
	private final DepotJpaRepository depotJpaRepository;
	private final DtoDepotToDepotMapper dtoDepotToDepotMapper;
	
	@Autowired
	public DepotSaveController(
			BankJpaRepository bankJpaRepository,
			DepotJpaRepository depotJpaRepository,
			DtoDepotToDepotMapper dtoDepotToDepotMapper) {
		
		this.bankJpaRepository = bankJpaRepository;
		this.depotJpaRepository = depotJpaRepository;
		this.dtoDepotToDepotMapper = dtoDepotToDepotMapper;
	}
	
	public void saveOrUpdate(DtoDepot dtoDepot) {
		Bank bank = bankJpaRepository.getOne(dtoDepot.getBank().getId());
		
		Depot depot = createOrUpdateDepot(dtoDepot, bank);
		depotJpaRepository.save(depot);
	}
	
	private Depot createOrUpdateDepot(DtoDepot dtoDepot, Bank bank) {
		if (dtoDepot.getId() == null) {
			return dtoDepotToDepotMapper.map(bank, dtoDepot);
		} else {
			Depot depot = depotJpaRepository.getOne(dtoDepot.getId());
			dtoDepotToDepotMapper.update(depot, bank, dtoDepot);
			return depot;
		}
	}
}
