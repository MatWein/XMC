package org.xmc.be.services.cashaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Category;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.entities.cashaccount.CashAccountTransaction;
import org.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import org.xmc.be.repositories.category.CategoryJpaRepository;
import org.xmc.be.services.cashaccount.mapper.DtoCashAccountTransactionToCashAccountTransactionMapper;
import org.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;

@Component
public class CashAccountTransactionSaveController {
    private final CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository;
    private final DtoCashAccountTransactionToCashAccountTransactionMapper dtoCashAccountTransactionToCashAccountTransactionMapper;
    private final CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater;
    private final CategoryJpaRepository categoryJpaRepository;

    @Autowired
    public CashAccountTransactionSaveController(
            CashAccountTransactionJpaRepository cashAccountTransactionJpaRepository,
            DtoCashAccountTransactionToCashAccountTransactionMapper dtoCashAccountTransactionToCashAccountTransactionMapper,
            CashAccountTransactionSaldoUpdater cashAccountTransactionSaldoUpdater,
            CategoryJpaRepository categoryJpaRepository) {

        this.cashAccountTransactionJpaRepository = cashAccountTransactionJpaRepository;
        this.dtoCashAccountTransactionToCashAccountTransactionMapper = dtoCashAccountTransactionToCashAccountTransactionMapper;
        this.cashAccountTransactionSaldoUpdater = cashAccountTransactionSaldoUpdater;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    public void saveOrUpdate(CashAccount cashAccount, DtoCashAccountTransaction dtoTransaction) {
        Category category = dtoTransaction.getCategory() == null ? null : categoryJpaRepository.getOne(dtoTransaction.getCategory().getId());

        CashAccountTransaction cashAccountTransaction = createOrUpdateCashAccountTransaction(dtoTransaction, category, cashAccount);
        cashAccountTransactionJpaRepository.save(cashAccountTransaction);

        cashAccountTransactionSaldoUpdater.updateAll(cashAccountTransaction);
    }

    private CashAccountTransaction createOrUpdateCashAccountTransaction(DtoCashAccountTransaction dtoTransaction, Category category, CashAccount cashAccount) {
        if (dtoTransaction.getId() == null) {
            return dtoCashAccountTransactionToCashAccountTransactionMapper.map(cashAccount, category, dtoTransaction);
        } else {
            CashAccountTransaction cashAccountTransaction = cashAccountTransactionJpaRepository.getOne(dtoTransaction.getId());
            dtoCashAccountTransactionToCashAccountTransactionMapper.update(cashAccountTransaction, category, dtoTransaction);
            return cashAccountTransaction;
        }
    }
}
