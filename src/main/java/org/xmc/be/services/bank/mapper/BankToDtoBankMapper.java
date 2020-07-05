package org.xmc.be.services.bank.mapper;

import org.springframework.stereotype.Component;
import org.xmc.be.entities.Bank;
import org.xmc.be.entities.BinaryData;
import org.xmc.common.stubs.DtoBank;

import java.util.function.Function;

@Component
public class BankToDtoBankMapper implements Function<Bank, DtoBank> {
    @Override
    public DtoBank apply(Bank bank) {
        DtoBank dtoBank = new DtoBank();

        dtoBank.setBic(bank.getBic());
        dtoBank.setBlz(bank.getBlz());
        dtoBank.setId(bank.getId());
        dtoBank.setName(bank.getName());

        BinaryData logo = bank.getLogo();
        if (logo != null) {
            dtoBank.setLogo(logo.getRawData());
        }

        return dtoBank;
    }
}
