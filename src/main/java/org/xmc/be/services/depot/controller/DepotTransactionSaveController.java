package org.xmc.be.services.depot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.Depot;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import org.xmc.be.services.depot.mapper.DtoDepotTransactionToDepotTransactionMapper;
import org.xmc.common.stubs.depot.transactions.DtoDepotTransaction;

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
			DepotTransaction depotTransaction = depotTransactionJpaRepository.getOne(dtoTransaction.getId());
			dtoDepotTransactionToDepotTransactionMapper.update(depotTransaction, dtoTransaction);
			return depotTransaction;
		}
	}
}
