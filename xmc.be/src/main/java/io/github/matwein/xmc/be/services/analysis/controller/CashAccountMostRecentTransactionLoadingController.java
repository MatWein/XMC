package io.github.matwein.xmc.be.services.analysis.controller;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.services.analysis.calculation.MostRecentTransactionsCalculator;
import io.github.matwein.xmc.be.services.analysis.mapper.CashAccountTransactionToDtoMostRecentTransactionMapper;
import io.github.matwein.xmc.common.stubs.analysis.DtoMostRecentTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashAccountMostRecentTransactionLoadingController {
	private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
	private final CashAccountTransactionToDtoMostRecentTransactionMapper cashAccountTransactionToDtoMostRecentTransactionMapper;
	
	@Autowired
	public CashAccountMostRecentTransactionLoadingController(
			CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
			CashAccountTransactionToDtoMostRecentTransactionMapper cashAccountTransactionToDtoMostRecentTransactionMapper) {
		
		this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
		this.cashAccountTransactionToDtoMostRecentTransactionMapper = cashAccountTransactionToDtoMostRecentTransactionMapper;
	}
	
	public List<DtoMostRecentTransaction> loadTransactionsForCashAccounts(List<Long> cashAccountIds) {
		List<CashAccountTransaction> mostRecentTransactions = cashAccountTransactionJpaRepository.findMostRecentTransactions(
				cashAccountIds,
				PageRequest.of(0, MostRecentTransactionsCalculator.MAX_TRANSACTIONS));
		
		return cashAccountTransactionToDtoMostRecentTransactionMapper.mapAll(mostRecentTransactions);
	}
}
