package org.xmc.be.services.bank.mapper;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.be.entities.Bank;
import org.xmc.common.stubs.bank.DtoBank;

class BankToDtoBankMapperTest extends JUnitTestBase {
    private BankToDtoBankMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BankToDtoBankMapper();
    }

    @Test
    void testApply() {
        Bank bank = testObjectFactory.createRandom(Bank.class);

        DtoBank result = mapper.apply(bank);

        Assert.assertSame(bank.getBic(), result.getBic());
        Assert.assertSame(bank.getBlz(), result.getBlz());
        Assert.assertSame(bank.getId(), result.getId());
        Assert.assertSame(bank.getName(), result.getName());
        Assert.assertSame(bank.getLogo().getRawData(), result.getLogo());
    }

    @Test
    void testApply_LogoNull() {
        Bank bank = testObjectFactory.createRandom(Bank.class);
        bank.setLogo(null);

        DtoBank result = mapper.apply(bank);

        Assert.assertSame(bank.getBic(), result.getBic());
        Assert.assertSame(bank.getBlz(), result.getBlz());
        Assert.assertSame(bank.getId(), result.getId());
        Assert.assertSame(bank.getName(), result.getName());
        Assert.assertNull(result.getLogo());
    }
}