package org.xmc.be.services.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xmc.be.repositories.bank.BankJpaRepository;
import org.xmc.be.services.bank.mapper.BankToDtoBankMapper;
import org.xmc.common.stubs.DtoBank;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankService {
    private final BankJpaRepository bankJpaRepository;
    private final BankToDtoBankMapper bankToDtoBankMapper;

    @Autowired
    public BankService(BankJpaRepository bankJpaRepository, BankToDtoBankMapper bankToDtoBankMapper) {
        this.bankJpaRepository = bankJpaRepository;
        this.bankToDtoBankMapper = bankToDtoBankMapper;
    }

    public List<DtoBank> loadAllBanks() {
        return bankJpaRepository.findAll().stream()
                .map(bankToDtoBankMapper)
                .collect(Collectors.toList());
    }
}
