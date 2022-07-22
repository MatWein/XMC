package io.github.matwein.xmc.be.services.depot.controller;

import io.github.matwein.xmc.be.entities.depot.Depot;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import io.github.matwein.xmc.be.services.depot.mapper.DtoDepotTransactionToDepotTransactionMapper;
import io.github.matwein.xmc.common.stubs.depot.transactions.DtoDepotTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepotTransactionSaveController {
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	private final DtoDepotTransactionToDepotTransactionMapper dtoDepotTransactionToDepotTransactionMapper;
	
	@Autowired
	public DepotTransactionSaveController(
			DepotTransactionJpaRepository depotTransactionJpaRepository,
			DtoDepotTransactionToDepotTransactionMapper dtoDepotTransactionToDepotTransactionMapper) {
		
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
		this.dtoDepotTransactionToDepotTransactionMapper = dtoDepotTransactionToDepotTransactionMapper;
	}
	
	public void saveOrUpdate(Depot depot, DtoDepotTransaction dtoDepotTransaction) {
		DepotTransaction depotTransaction = createOrUpdateDepotTransaction(dtoDepotTransaction, depot);
		depotTransactionJpaRepository.save(depotTransaction);
	}
	
	private DepotTransaction createOrUpdateDepotTransaction(DtoDepotTransaction dtoTransaction, Depot depot) {
		if (dtoTransaction.getId() == null) {
			return dtoDepotTransactionToDepotTransactionMapper.map(depot, dtoTransaction);
		} else {
			DepotTransaction depotTransaction = depotTransactionJpaRepository.getReferenceById(dtoTransaction.getId());
			dtoDepotTransactionToDepotTransactionMapper.update(depotTransaction, dtoTransaction);
			return depotTransaction;
		}
	}
}
