package io.github.matwein.xmc.be.services.cashaccount.controller;

import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccountTransaction;
import io.github.matwein.xmc.be.entities.cashaccount.Category;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountTransactionJpaRepository;
import io.github.matwein.xmc.be.repositories.category.CategoryJpaRepository;
import io.github.matwein.xmc.be.services.cashaccount.mapper.DtoCashAccountTransactionToCashAccountTransactionMapper;
import io.github.matwein.xmc.common.stubs.cashaccount.transactions.DtoCashAccountTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        Category category = dtoTransaction.getCategory() == null ? null : categoryJpaRepository.getReferenceById(dtoTransaction.getCategory().getId());

        CashAccountTransaction cashAccountTransaction = createOrUpdateCashAccountTransaction(dtoTransaction, category, cashAccount);
        cashAccountTransactionJpaRepository.save(cashAccountTransaction);

        cashAccountTransactionSaldoUpdater.updateAll(cashAccountTransaction);
    }

    private CashAccountTransaction createOrUpdateCashAccountTransaction(DtoCashAccountTransaction dtoTransaction, Category category, CashAccount cashAccount) {
        if (dtoTransaction.getId() == null) {
            return dtoCashAccountTransactionToCashAccountTransactionMapper.map(cashAccount, category, dtoTransaction);
        } else {
            CashAccountTransaction cashAccountTransaction = cashAccountTransactionJpaRepository.getReferenceById(dtoTransaction.getId());
            dtoCashAccountTransactionToCashAccountTransactionMapper.update(cashAccountTransaction, category, dtoTransaction);
            return cashAccountTransaction;
        }
    }
}
