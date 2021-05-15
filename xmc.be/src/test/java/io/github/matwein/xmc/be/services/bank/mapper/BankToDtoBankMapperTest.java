package io.github.matwein.xmc.be.services.bank.mapper;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.matwein.xmc.be.entities.Bank;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankToDtoBankMapperTest {
    private BankToDtoBankMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BankToDtoBankMapper();
    }

    @Test
    void testApply() {
        Bank bank = EnhancedRandomBuilder.aNewEnhancedRandom().nextObject(Bank.class);

        DtoBank result = mapper.apply(bank);

        Assertions.assertSame(bank.getBic(), result.getBic());
        Assertions.assertSame(bank.getBlz(), result.getBlz());
        Assertions.assertSame(bank.getId(), result.getId());
        Assertions.assertSame(bank.getName(), result.getName());
        Assertions.assertSame(bank.getLogo().getRawData(), result.getLogo());
    }

    @Test
    void testApply_LogoNull() {
        Bank bank = EnhancedRandomBuilder.aNewEnhancedRandom().nextObject(Bank.class);
        bank.setLogo(null);

        DtoBank result = mapper.apply(bank);

        Assertions.assertSame(bank.getBic(), result.getBic());
        Assertions.assertSame(bank.getBlz(), result.getBlz());
        Assertions.assertSame(bank.getId(), result.getId());
        Assertions.assertSame(bank.getName(), result.getName());
        Assertions.assertNull(result.getLogo());
    }
}