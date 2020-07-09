package org.xmc.be.services.cashaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.repositories.bank.BankJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.services.cashaccount.mapper.DtoCashAccountToCashAccountMapper;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;

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
        Bank bank = bankJpaRepository.getOne(dtoCashAccount.getBank().getId());

        CashAccount cashAccount = createOrUpdateCashAccount(dtoCashAccount, bank);
        cashAccountJpaRepository.save(cashAccount);
    }

    private CashAccount createOrUpdateCashAccount(DtoCashAccount dtoCashAccount, Bank bank) {
        if (dtoCashAccount.getId() == null) {
            return dtoCashAccountToCashAccountMapper.map(bank, dtoCashAccount);
        } else {
            CashAccount cashAccount = cashAccountJpaRepository.getOne(dtoCashAccount.getId());
            dtoCashAccountToCashAccountMapper.update(cashAccount, bank, dtoCashAccount);
            return cashAccount;
        }
    }
}
