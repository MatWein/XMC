package io.github.matwein.xmc.be.services.cashaccount.controller;

import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.entities.cashaccount.CashAccount;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import io.github.matwein.xmc.be.services.cashaccount.mapper.DtoCashAccountToCashAccountMapper;
import io.github.matwein.xmc.common.stubs.cashaccount.DtoCashAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CashAccountSaveController {
    private final BankJpaRepository bankJpaRepository;
    private final DtoCashAccountToCashAccountMapper dtoCashAccountToCashAccountMapper;
    private final CashAccountJpaRepository cashAccountJpaRepository;

    @Autowired
    public CashAccountSaveController(
            BankJpaRepository bankJpaRepository,
            DtoCashAccountToCashAccountMapper dtoCashAccountToCashAccountMapper,
            CashAccountJpaRepository cashAccountJpaRepository) {

        this.bankJpaRepository = bankJpaRepository;
        this.dtoCashAccountToCashAccountMapper = dtoCashAccountToCashAccountMapper;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
    }

    public void saveOrUpdate(DtoCashAccount dtoCashAccount) {
        Bank bank = bankJpaRepository.getReferenceById(dtoCashAccount.getBank().getId());

        CashAccount cashAccount = createOrUpdateCashAccount(dtoCashAccount, bank);
        cashAccountJpaRepository.save(cashAccount);
    }

    private CashAccount createOrUpdateCashAccount(DtoCashAccount dtoCashAccount, Bank bank) {
        if (dtoCashAccount.getId() == null) {
            return dtoCashAccountToCashAccountMapper.map(bank, dtoCashAccount);
        } else {
            CashAccount cashAccount = cashAccountJpaRepository.getReferenceById(dtoCashAccount.getId());
            dtoCashAccountToCashAccountMapper.update(cashAccount, bank, dtoCashAccount);
            return cashAccount;
        }
    }
}
