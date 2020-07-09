package org.xmc.be.services.cashaccount.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.common.factories.BinaryDataFactory;
import org.xmc.be.entities.Bank;
import org.xmc.common.stubs.bank.DtoBank;
import org.xmc.common.utils.ImageUtil;

@Component
public class DtoBankToBankMapper {
    private static final String DESCRIPTION = "bank logo";
    private static final int IMAGE_SIZE = 24;

    private final BinaryDataFactory binaryDataFactory;

    @Autowired
    public DtoBankToBankMapper(BinaryDataFactory binaryDataFactory) {
        this.binaryDataFactory = binaryDataFactory;
    }

    public Bank map(DtoBank dtoBank) {
        Bank bank = new Bank();
        update(bank, dtoBank);
        return bank;
    }

    public void update(Bank bank, DtoBank dtoBank) {
        bank.setBic(dtoBank.getBic());
        bank.setBlz(dtoBank.getBlz());
        bank.setName(dtoBank.getName());

        if (dtoBank.getLogo() != null) {
            byte[] resizedLogo = ImageUtil.resize$(dtoBank.getLogo(), IMAGE_SIZE, IMAGE_SIZE);
            bank.setLogo(binaryDataFactory.create(resizedLogo, DESCRIPTION));
        }
    }
}
