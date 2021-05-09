package io.github.matwein.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.be.entities.depot.DepotTransaction;
import io.github.matwein.xmc.be.repositories.depot.DepotTransactionJpaRepository;
import io.github.matwein.xmc.be.services.analysis.calculation.MostRecentTransactionsCalculator;
import io.github.matwein.xmc.be.services.analysis.mapper.DepotTransactionToDtoMostRecentTransactionMapper;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;

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
