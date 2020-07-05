package org.xmc.be.services.cashaccount.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.cashaccount.CashAccount;
import org.xmc.be.repositories.BinaryDataJpaRepository;
import org.xmc.be.repositories.bank.BankJpaRepository;
import org.xmc.be.repositories.cashaccount.CashAccountJpaRepository;
import org.xmc.be.services.cashaccount.mapper.DtoBankToBankMapper;
import org.xmc.be.services.cashaccount.mapper.DtoCashAccountToCashAccountMapper;
import org.xmc.common.stubs.cashaccount.DtoCashAccount;

@Component
public class CashAccountSaveController {
    private final DtoBankToBankMapper dtoBankToBankMapper;
    private final BankJpaRepository bankJpaRepository;
    private final DtoCashAccountToCashAccountMapper dtoCashAccountToCashAccountMapper;
    private final CashAccountJpaRepository cashAccountJpaRepository;
    private final BinaryDataJpaRepository binaryDataJpaRepository;

    @Autowired
    public CashAccountSaveController(
            DtoBankToBankMapper dtoBankToBankMapper,
            BankJpaRepository bankJpaRepository,
            DtoCashAccountToCashAccountMapper dtoCashAccountToCashAccountMapper,
            CashAccountJpaRepository cashAccountJpaRepository,
            BinaryDataJpaRepository binaryDataJpaRepository) {

        this.dtoBankToBankMapper = dtoBankToBankMapper;
        this.bankJpaRepository = bankJpaRepository;
        this.dtoCashAccountToCashAccountMapper = dtoCashAccountToCashAccountMapper;
        this.cashAccountJpaRepository = cashAccountJpaRepository;
        this.binaryDataJpaRepository = binaryDataJpaRepository;
    }

    public void saveOrUpdate(DtoCashAccount dtoCashAccount) {
        Bank bank = createOrLoadBank(dtoCashAccount);

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

    private Bank createOrLoadBank(DtoCashAccount dtoCashAccount) {
        if (dtoCashAccount.getBank().getId() == null) {
            Bank bank = dtoBankToBankMapper.map(dtoCashAccount.getBank());
            if (bank.getLogo() != null) {
                binaryDataJpaRepository.save(bank.getLogo());
            }

            bankJpaRepository.save(bank);
            return bank;
        } else {
            return bankJpaRepository.getOne(dtoCashAccount.getBank().getId());
        }
    }
}
