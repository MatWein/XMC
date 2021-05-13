package io.github.matwein.xmc.be.services.bank.controller;

import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.be.repositories.BinaryDataJpaRepository;
import io.github.matwein.xmc.be.repositories.bank.BankJpaRepository;
import io.github.matwein.xmc.be.services.bank.mapper.DtoBankToBankMapper;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankSaveController {
    private final DtoBankToBankMapper dtoBankToBankMapper;
    private final BankJpaRepository bankJpaRepository;
    private final BinaryDataJpaRepository binaryDataJpaRepository;

    @Autowired
    public BankSaveController(
            DtoBankToBankMapper dtoBankToBankMapper,
            BankJpaRepository bankJpaRepository,
            BinaryDataJpaRepository binaryDataJpaRepository) {

        this.dtoBankToBankMapper = dtoBankToBankMapper;
        this.bankJpaRepository = bankJpaRepository;
        this.binaryDataJpaRepository = binaryDataJpaRepository;
    }

    public void saveOrUpdate(DtoBank dtoBank) {
        Bank bank = createOrUpdateBank(dtoBank);

        if (bank.getLogo() != null) {
            binaryDataJpaRepository.save(bank.getLogo());
        }
        bankJpaRepository.save(bank);
    }

    private Bank createOrUpdateBank(DtoBank dtoBank) {
        if (dtoBank.getId() == null) {
            return dtoBankToBankMapper.map(dtoBank);
        } else {
            Bank bank = bankJpaRepository.getOne(dtoBank.getId());
            dtoBankToBankMapper.update(bank, dtoBank);
            return bank;
        }
    }
}
