package org.xmc.be.services.cashaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;

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

    public void updateAll(LocalDate startDate) {
        cashAccountTransactionJpaRepository.flush();

        List<CashAccountTransaction> transactionsToUpdate = cashAccountTransactionJpaRepository.findByValutaDateGreaterThanEqualOrderByValutaDate(startDate);
        Optional<CashAccountTransaction> transactionBeforeCurrentDate = cashAccountTransactionJpaRepository.findFirstByValutaDateIsBeforeOrderByValutaDateDesc(startDate);

        BigDecimal saldoBefore = transactionBeforeCurrentDate.map(CashAccountTransaction::getSaldoAfter).orElse(new BigDecimal(0.0));

        for (CashAccountTransaction cashAccountTransaction : transactionsToUpdate) {
            cashAccountTransaction.setSaldoBefore(saldoBefore);

            BigDecimal saldoAfter = saldoBefore.add(cashAccountTransaction.getValue());
            cashAccountTransaction.setSaldoAfter(saldoAfter);

            cashAccountTransactionJpaRepository.save(cashAccountTransaction);
            saldoBefore = saldoAfter;
        }
    }
}
