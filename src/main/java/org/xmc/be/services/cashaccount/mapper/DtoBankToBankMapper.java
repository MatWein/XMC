package org.xmc.be.services.cashaccount.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.common.factories.BinaryDataFactory;
import org.xmc.be.entities.Bank;
import org.xmc.common.stubs.DtoBank;

@Component
public class DtoBankToBankMapper {
    private static final String DESCRIPTION = "bank logo";

    private final BinaryDataFactory binaryDataFactory;

    @Autowired
    public DtoBankToBankMapper(BinaryDataFactory binaryDataFactory) {
        this.binaryDataFactory = binaryDataFactory;
    }

    public Bank map(DtoBank dtoBank) {
        Bank bank = new Bank();

        bank.setBic(dtoBank.getBic());
        bank.setBlz(dtoBank.getBlz());
        bank.setName(dtoBank.getName());
        bank.setLogo(binaryDataFactory.create(dtoBank.getLogo(), DESCRIPTION));

        return bank;
    }
}
