package io.github.matwein.xmc.be.services.cashaccount.controller;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class CashAccountTransactionSaldoUpdater {
    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;

    @Autowired
    public CashAccountTransactionSaldoUpdater(CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository) {
        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
    }

    public void updateAll(CashAccountTransaction transaction) {
        if (transaction == null) {
            return;
        }

        updateAll(transaction.getCashAccount(), transaction.getValutaDate());
    }

    public void updateAll(CashAccount cashAccount, LocalDate startDate) {
        cashAccountTransactionJpaRepository.flush();

        List<CashAccountTransaction> transactionsToUpdate = cashAccountTransactionJpaRepository.findTransactionsAfterDate(cashAccount, startDate);
        if (transactionsToUpdate.isEmpty()) {
            return;
        }

        BigDecimal saldoBefore = calculateSaldoBefore(cashAccount, transactionsToUpdate.get(0).getValutaDate());

        for (CashAccountTransaction cashAccountTransaction : transactionsToUpdate) {
            cashAccountTransaction.setSaldoBefore(saldoBefore);

            BigDecimal saldoAfter = calculateSaldoAfter(saldoBefore, cashAccountTransaction.getValue());
            cashAccountTransaction.setSaldoAfter(saldoAfter);

            cashAccountTransactionJpaRepository.save(cashAccountTransaction);
            saldoBefore = saldoAfter;
        }
	
        if (CollectionUtils.isNotEmpty(transactionsToUpdate)) {
	        CashAccountTransaction mostRecentTransaction = transactionsToUpdate.get(transactionsToUpdate.size() - 1);
	        
	        cashAccount.setLastSaldo(mostRecentTransaction.getSaldoAfter());
	        cashAccount.setLastSaldoDate(mostRecentTransaction.getValutaDate());
        }
    }

    public BigDecimal calculateSaldoBefore(CashAccount cashAccount, LocalDate valutaDate) {
        Optional<CashAccountTransaction> transactionBeforeCurrentDate = cashAccountTransactionJpaRepository.findFirstTransactionBeforeDate(cashAccount, valutaDate);
        return transactionBeforeCurrentDate.map(CashAccountTransaction::getSaldoAfter).orElse(BigDecimal.valueOf(0.0));
    }

    public BigDecimal calculateSaldoAfter(BigDecimal saldoBefore, BigDecimal value) {
        return saldoBefore.add(value);
    }
}
