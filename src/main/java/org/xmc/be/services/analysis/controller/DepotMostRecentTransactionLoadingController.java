package org.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.depot.DepotTransaction;
import org.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import org.xmc.be.services.analysis.calculation.MostRecentTransactionsCalculator;
import org.xmc.be.services.analysis.mapper.DepotTransactionToDtoMostRecentTransactionMapper;
import org.xmc.common.stubs.analysis.DtoMostRecentTransaction;

import java.util.List;

@Component
public class DepotMostRecentTransactionLoadingController {
	private final DepotTransactionJpaRepository depotTransactionJpaRepository;
	private final DepotTransactionToDtoMostRecentTransactionMapper depotTransactionToDtoMostRecentTransactionMapper;
	
	@Autowired
	public DepotMostRecentTransactionLoadingController(
			DepotTransactionJpaRepository depotTransactionJpaRepository,
			DepotTransactionToDtoMostRecentTransactionMapper depotTransactionToDtoMostRecentTransactionMapper) {
		
		this.depotTransactionJpaRepository = depotTransactionJpaRepository;
		this.depotTransactionToDtoMostRecentTransactionMapper = depotTransactionToDtoMostRecentTransactionMapper;
	}
	
	public List<DtoMostRecentTransaction> loadTransactionsForDepots(List<Long> depotIds) {
		List<DepotTransaction> mostRecentTransactions = depotTransactionJpaRepository.findMostRecentTransactions(
				depotIds,
				PageRequest.of(0, MostRecentTransactionsCalculator.MAX_TRANSACTIONS));
		
		return depotTransactionToDtoMostRecentTransactionMapper.mapAll(mostRecentTransactions);
	}
}
