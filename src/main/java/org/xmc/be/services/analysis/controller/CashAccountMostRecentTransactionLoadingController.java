package org.xmc.be.services.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.services.analysis.calculation.MostRecentTransactionsCalculator;
import org.xmc.be.services.analysis.mapper.CashAccountTransactionToDtoMostRecentTransactionMapper;
import org.xmc.common.stubs.analysis.DtoMostRecentTransaction;

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
