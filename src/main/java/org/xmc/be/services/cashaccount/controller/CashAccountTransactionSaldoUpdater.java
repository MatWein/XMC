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
        BigDecimal saldoBefore = calculateSaldoBefore(startDate);

        for (CashAccountTransaction cashAccountTransaction : transactionsToUpdate) {
            cashAccountTransaction.setSaldoBefore(saldoBefore);

            BigDecimal saldoAfter = calculateSaldoAfter(saldoBefore, cashAccountTransaction.getValue());
            cashAccountTransaction.setSaldoAfter(saldoAfter);

            cashAccountTransactionJpaRepository.save(cashAccountTransaction);
            saldoBefore = saldoAfter;
        }
    }

    public BigDecimal calculateSaldoBefore(LocalDate startDate) {
        Optional<CashAccountTransaction> transactionBeforeCurrentDate = cashAccountTransactionJpaRepository.findFirstByValutaDateIsBeforeOrderByValutaDateDesc(startDate);
        return transactionBeforeCurrentDate.map(CashAccountTransaction::getSaldoAfter).orElse(new BigDecimal(0.0));
    }

    public BigDecimal calculateSaldoAfter(BigDecimal saldoBefore, BigDecimal value) {
        return saldoBefore.add(value);
    }
}
